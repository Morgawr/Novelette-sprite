(ns novelette_sprite.render
  (:require-macros [schema.core :as s])
  (:require [schema.core :as s]
            [novelette_sprite.schemas :as sc]))

(s/defn draw-sprite
  "Draw a sprite on the given canvas."
  [ctx :- js/CanvasRenderingContext2D
   sprite :- sc/Sprite
   data-bank :- {s/Keyword js/Image}]
  (let [image ((get-in sprite [:data :spritesheet]) data-bank)
        keyframe (get-in sprite [:data :sequence (:keyframe sprite) :coordinates])
        rot (:rotation sprite)
        scale (:scale sprite)
        new-width (* (keyframe 2) scale)
        new-height (* (keyframe 3) scale)]
    (.save ctx)
    (.translate ctx (get-in sprite [:position 0]) (get-in sprite [:position 1]))
    (.translate ctx (/ new-width 2) (/ new-height 2)) ; translate context origin to center point of the sprite
    (.rotate ctx rot)
    (.drawImage ctx image (keyframe 0) (keyframe 1) (keyframe 2) (keyframe 3)
                (/ (- new-width) 2)
                (/ (- new-height) 2) ; render in the new coordinate system of the translated canvas
                new-width new-height) ; stretch to perform uniform scaling
    (.restore ctx))) ; restore context location and rotation to its original place
