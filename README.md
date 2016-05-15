# AppAmbekar

## Synopsis

AppAmbekar is a completely native Android application I have created to present myself as an Android developer. 
It includes several custom interface elements, is connected to a backend service, and has a few external modules integrated into the project.

## Custom Interfaces

I have created a few custom interfaces to outline the customizability Android offers and to demonstrate my ability to create unique User Experiences.
The 2 most interesting ones are The ParallaxSkyLayout & The HeaderContentHSV.


#### The ParallaxSkyLayout

This layout is used present the Introduction, and is the first screen that loads up with the app. 
It creates an effect moving from day to night as the user scrolls down the view, while providing an interesting effect of clouds moving.

**Involves manual bitmap manipulation based on the scroll position of the registered scroll view.**


#### The HeaderContentHSV

This element is used to present My Favorite Libraries. 
It can be populated with content groups with headers and provides a means for parent views/fragments/activities to respond to user click events.
It also snaps the headers for the groups as the user scrolls through the content.

**Involves identifying visible views, calculation positions and manually re-position views on scrolling**


## Backend & Hosting Services

I power my backend with a BasS (Backend As A Service) called [Firebase](https://www.firebase.com/) to demonstrate the simplicity of extending an apps with third party backend services.
I am currently storing all data regarding my Completed Projects and Contact Info on Firebase.
This allows me to easily update my contact info or add to new projects as I complete them to the backend without having to update the application on Google Play.

I am using [Cloudinary](http://cloudinary.com/) to host all images and graphics related to my Completed Projects.
It provides reliable and fast access to my files on demand as will as some simple image manipulation services.


## External Modules

I have integrated 2 external modules into my project: The SmartTextView & The ViewPagerIndicator.

#### The SmartTextView

This is a module I created myself, extending the standard Android TextView. 
The most notable feature it provides is the ability to justify text! A common method of rendering text that Android does not provide.
It can also justify single words to span the entire width of the view.
Additionally it can register 3 typefaces for font styles: Thin, Regular & Think. These can be directly set using XML attributes and simplify font management.

**Text justification involves word boundary calculations, and manual word and/or letter placement on canvas**

#### The ViewPagerIndicator

This is a third party module that can be found here: http://viewpagerindicator.com/

