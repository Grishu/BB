Drag & Drop Views in Android
================================


QuickView (Highlighting Features)
----------------------------------------
-Allow users to move data within your Activity layout using graphical gestures.
-Supports operations besides data movement.
-Only works within a single application.
-Requires API 11.

     
      A drag and drop operation starts when the user makes some gesture that you recognize as a signal to start
      dragging data. In response, your application tells the system that the drag is starting. The system calls back
      to your application to get a representation of the data being dragged. As the user's finger moves this
      representation (a "drag shadow") over the current layout, the system sends drag events to the drag event
      listener objects and drag event callback methods associated with the View objects in the layout.
      Once the user releases the drag shadow, the system ends the drag operation.

       You create a drag event listener object ("listeners") from a class that implements View.OnDragListener.
       You set the drag event listener object for a View with the View object'ssetOnDragListener() method. 
       Each View object also has aonDragEvent() callback method.
       
When you start a drag, you include both the data you are moving and metadata describing this data as part of the call to the system. During the drag, the system sends drag events to the drag event listeners or callback methods of each View in the layout. The listeners or callback methods can use the metadata to decide if they want to accept the data when it is dropped.

![SS1](http://4.bp.blogspot.com/-0J2KoIb8sNY/UonxqWtdKtI/AAAAAAAAAKI/T_xLKqlftig/s1600/1.png)
![SS2](http://1.bp.blogspot.com/-0jYxkbQlWxA/UonxrEYqlJI/AAAAAAAAAKQ/SOJXub-QWjA/s1600/2.PNG)
