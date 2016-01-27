(ns novelette_sprite.schemas
  (:require-macros [schema.core :as s])
  (:require [schema.core :as s]))

(s/defschema function (s/pred fn? 'fn?))

; A position is either a pair of coordinates x/y or a tuple of four values x/y/w/h
; packed into a vector.
(s/defschema pos (s/conditional
                   #(= (count %) 2) [(s/one s/Int :x) (s/one s/Int :y)]
                   #(= (count %) 4) [(s/one s/Int :x)
                                     (s/one s/Int :y)
                                     (s/one s/Int :width)
                                     (s/one s/Int :height)]))

; The id of an element can either be a string or a keyword (prefer using keywords).
(s/defschema id (s/cond-pre s/Str s/Keyword))

; A sprite is different from an image, an image is a texture loaded into the
; engine's renderer with an id assigned as a reference. A sprite is an instance
; of a texture paired with appropriate positioning and rendering data
; with animations.

(s/defrecord Keyframe
  [coordinates :- pos ; Rectangle of coordinates in the spritesheet
   delay :- s/Int ; Amount of milliseconds before the next keyframe plays
   ])

(s/defrecord SpriteModel
  [spritesheet :- id ; Id of the spritesheet used as reference
   sequence :- [Keyframe] ; Animation sequence
   loop? :- s/Bool ; Does the animation loop once it ends?
   tween? :- s/Bool ; Does the engine need to apply tweening? (TODO not implemented yet)
   ])

(s/defrecord Sprite
  [data :- SpriteModel
   position :- pos
   keyframe :- s/Int ; The current keyframe index in the animation
   elapsed :- s/Int ; how much time has elapsed for the current frame (in ms)
   scale :- s/Num
   rotation :- s/Num
   z-index :- s/Int ; depth ordering for rendering, lower = front
   ])
