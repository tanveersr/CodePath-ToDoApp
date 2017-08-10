# Pre-work - ToDoApp

ToDoApp is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Tanveer Randhawa

Time spent: 15 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (and display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)



## Video Walkthrough 

Here's a walkthrough of implemented user stories:

![YAP](http://i.imgur.com/sVSdmsP.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** I feel that the implementation of layouts in Android platform is quite developer-friendly. I like the fact that it provides variety of options for developer to choose from based on the specific need of the UI for their applications. Also, the high level of abstraction the framework provides makes it possible to construct the UI in far less time in an easy to define format (xml definition). Compared to the traditional ways of defining layouts for web development using html/css/js based frameworks the layouts in android platform are more well defined but they well meet the needs of the mobile app design.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** ArrayAdapter used in the pre-work plays the role of an interface linking the datastructure containing the intended contents to be populated in the list (in this case an ArrayList of ToDoItems) to the listview. By allowing user to define and pass a layout, the adapter decouples the logic of desigining the ui within the elements of the list. One can change the look of the listview elements simply by passing a different layout with no impact to the rest of the logic.
The convertView allows us to reuse previously created view objects. Since, in a list view multiple elements will use the same view (for consistent rendering) convertView will allow using the same view object for subsequent elements in the list instead of creating new view objects each time. This improves the performance.

## License

    Copyright [2017] [Tanveer Randhawa]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
