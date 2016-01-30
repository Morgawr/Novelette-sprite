# novelette-sprite

Clojurescript canvas 2D sprite and animation handling library.
This library is intended as a standalone "plug and play" 2D rendering library
to be mainly used by the Novelette project.

It is currently in beta and is being developed alongside the Novelette project.

## Usage

[![Clojars Project](http://clojars.org/novelette-sprite/latest-version.svg)](http://clojars.org/novelette-sprite)

While there is no full manual or examples yet, I will give a quick rundown to the structure of the library.

The library is divided in two main files, the loader and the renderer.

The loader is a standalone resource loader that takes a list of identifiers (keywords) and a list of names (URL resource addresses) of pictures. The loader loads each picture one by one and then can be queried to retrieve the full state of images (it is the only stateful part of the library).

The renderer is the part of the library that takes care of rendering the 2D images on screen and provides tools to control the sprite animations like playing and pausing them or updating them.

Novelette-sprite works on two main data structures, the SpriteModel and the Sprite. A SpriteModel is an abstract metadata that contains a series of keyframes (for a 2D animation), a reference to a spritesheet and some extra parameters that define a general sprite. A Sprite is an instantiation of a SpriteModel and contains object data like current animation, timestamp, whether or not the animation is playing and the position on the screen.

By instantiating the Sprite with a definition of a SpriteModel, it is possible to pass the Sprite around to the renderer functions and animate/display the 2D sprites on screen.

## License

Copyright © 2016 Federico "Morg" Pareschi

Distributed under the MIT Free Software License.
