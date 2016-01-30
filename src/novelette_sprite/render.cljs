(ns novelette-sprite.render
  (:require-macros [schema.core :as s])
  (:require [schema.core :as s]
            [novelette-sprite.schemas :as sc]))

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

(s/defn update-sprite
  "Update the sprite and its animation."
  [sprite :- sc/Sprite
   elapsed :- s/Num]
  (let [update-keyframe
        (fn [s]
          (let [frames (get-in s [:data :sequence])
                index (:keyframe s)
                elapsed (:elapsed s)
                loop? (get-in s [:data :loop?])
                curr-frame (frames index)
                next-index (if (>= (inc index) (count frames)) 0 (inc index))
                next-frame (frames next-index)]
            (cond
              (not (:active? s)) s
              (and (not loop?) (= 0 next-index))
              (assoc s :elapsed 0)
              (>= elapsed (:delay curr-frame))
              (assoc s
                     :elapsed (- elapsed (:delay curr-frame))
                     :keyframe next-index)
              :else s)))]
    (-> sprite
        (update :elapsed (partial + elapsed))
        (update-keyframe))))

(s/defn stop-sprite
  "Stop the sprite animation and reset its keyframe to 0."
  [sprite :- sc/Sprite]
  (assoc sprite :active? false :keyframe 0 :elapsed 0))

(s/defn pause-sprite
  "Pause/resume the sprite animation."
  [sprite :- sc/Sprite]
  (update sprite :active? not))

(s/defn start-sprite
  "(Re)-Start the sprite animation."
  [sprite :- sc/Sprite]
  (assoc sprite :active? true :keyframe 0 :elapsed 0))
