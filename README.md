# OnDragIssue
This is a demonstration of a bug with [FilePicker function by Wavesonics](https://github.com/Wavesonics/compose-multiplatform-file-picker).

If you try to drag elements out of dialog in MP file picker after opening drag and drop screen, application will be frozen. 

AWT file picker work fine in this situation. 

This problem appears only after opening drag and drop window. 

You have to only open drag and drop screen to reproduce this bug
