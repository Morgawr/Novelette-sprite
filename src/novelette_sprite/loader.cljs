(ns novelette_sprite.loader
  (:require-macros [schema.core :as s])
  (:require [goog.dom :as dom]
            [schema.core :as s]
            [novelette_sprite.schemas :as sc]))

; This file takes care of loading image resources.
; It provides functions and callbacks to load various images and their given
; id representation.
; It stores the currently-loading sequence of images into a global atom
; that acts as a buffer. After all images are loaded it is possible to retrieve
; the whole state of the buffer and flush it for further loading.

(def data-buffer (atom {:to-load 0
                        :loaded 0
                        :data {}})) ; Global buffer of images currently being loaded.

(declare load-image)

(s/defn load-error
  "Callback in case there is a loading hiccup."
  [uri :- s/Str
   sym :- sc/id]
  (let [window (dom/getWindow)]
    (.setTimeout window #(load-image uri sym) 200)))

(s/defn load-image
  "Load the given image under the given keyword in the engine's buffer."
  [uri :- s/Str
   sym :- sc/id]
  (let [image (js/Image. )
        on-load (fn []
                  (swap! data-buffer assoc-in [:data sym] image)
                  (swap! data-buffer update :loaded inc))]
    (set! (.-onload image) on-load)
    (set! (.-onerror image) #(load-error uri sym))
    (set! (.-src image) uri)))

(s/defn loading-complete?
  "Return whether or not the current image loading operation has completed."
  []
  (= (:to-load @data-buffer) (:loaded @data-buffer)))

(s/defn load-images!
  "Load a list of images into the temporary data buffer."
  [images :- {sc/id s/Str}]
  (swap! data-buffer assoc :to-load (count images))
  (doseq [[k v] images]
    (load-image v k)))

(s/defn get-images!
  "Return the loaded images if loading is complete and reset
  the internal state."
  []
  (when (loading-complete?)
    (let [buffer @data-buffer]
      (reset! data-buffer {:to-load 0 :loaded 0 :data {}})
      (:data buffer))))

(s/defn create-sprite
  "Return a new instance of a sprite with the proper initial parameters."
  ([model :- sc/SpriteModel
    position :- sc/pos
    scale :- s/Num
    rot :- s/Num
    z-index :- s/Int]
   (sc/Sprite. model position 0 0 scale rot z-index))
  ([model :- sc/SpriteModel
    position :- sc/pos
    z-index :- s/Int]
   (create-sprite model position 1 0 z-index)))

(s/defn create-model
  "Return a new model for a sprite."
  ([texture-id :- s/Keyword
    keyframes :- [sc/Keyframe]
    looping? :- s/Bool]
   (sc/SpriteModel. texture-id keyframes looping? false))
  ([texture-id :- s/Keyword
    keyframes :- [sc/Keyframe]]
   (create-model texture-id keyframes true)))
