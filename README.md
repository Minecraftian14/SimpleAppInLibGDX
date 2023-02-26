# A Simple App Made In LibGDX

Hello everyone!!! This is a [libGDX](https://libgdx.com/) project made using a variety of tools. I hope that this readme will be
able to cover up most of the tech/tools I have used throughout the project.

- The project architecture was generated using [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff) which provided
a simple setup for lwjgl (desktop) and android builds via gradle. 
- Further, I used [Vis-UI](https://github.com/kotcrab/vis-ui) to design the skeleton for the GUI.
- To design the user interface, I created most of the images using [Pixelitor](https://github.com/lbalazscs/Pixelitor).
- This project has a lot of Nine Patches around the UI and even uses a few [Ten Patch](https://github.com/raeleus/TenPatch) elements too. 
- Finally, to draw a custom widget to show the plots, I used the [Shape Drawer](https://github.com/earlygrey/shapedrawer.

### A glimpse on the finished UI
<img src="https://user-images.githubusercontent.com/52451860/221395574-4ca66818-d52e-4895-a958-3745e6bec4d2.jpg" width="200" alt="login"/><img src="https://user-images.githubusercontent.com/52451860/221395593-5ac3db7a-0764-4d71-ae8a-52a859f38c7d.png" width="200" alt="signup"/><img src="https://user-images.githubusercontent.com/52451860/221395594-73b927b6-4ae1-487c-880a-021920f12328.png" width="200" alt="main"/>

I also used a bit of Shaders to give me a shock wave effect whenever a person clicks away form buttons.

<img src="https://user-images.githubusercontent.com/52451860/221395639-eef1cd7f-acea-48d1-86d8-8facf0e85840.jpg" width="200" alt="distort"/>

And the whole screen-to-screen transition is animated! Please view this screen record:

<img src="https://user-images.githubusercontent.com/52451860/221395633-44e98208-b15b-4833-9594-aa3d0c52613c.gif" width="200"/>

To run this project on desktop:
- `lwjgl3:run`: starts the application.
- `lwjgl3:dist`: creates a distributable.

To run this project on android:
- `android:run`: starts the application.
- `android:assembleRelease`: creates a distributable.