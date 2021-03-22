# tawk-android-jim 
## (20/25 points completed)
Simple Github User and Profile demonstration

### General Requirements
- [x] **Requirement 1**: In the first screen, the app has to fetch GitHub users list, parse it and display in the list
- [x] **Requirement 2**: Selecting a user has to fetch users profile data and open a profile view displaying the user's profile data
- [x] **Requirement 3**: The design must loosely follow the wireframe (at the bottom of this document).
- [x] **Requirement 4**: Own code logic should be commented on

### Generic Requirements
- [x] **Requirement 1**: Code must be done in Kotlin 1.4.x. using AndroidStudio.
- [x] **Requirement 2**: Data must be persisted using Room
- [x] **Requirement 3**: UI must be done using ConstraintLayout where appropriate.
- [x] **Requirement 4**: All network calls must be queued and limited to 1 request at a time.**
- [x] **Requirement 5**: All media has to be cached on disk
- [x] **Requirement 6**: Write Unit tests for data processing logic & models, Room models (validate creation & update).
- [ ] **Requirement 7**: Screen rotation change must be supported.

### Github Users
- [x] **Requirement 1**: The app has to be able to work offline if data has been previously loaded.
- [x] **Requirement 2**: The app must handle no internet scenario, show appropriate UI indicators.
- [x] **Requirement 3**: The app must automatically retry loading data once the connection is available.
- [x] **Requirement 4**: When there is data available (saved in the database) from previous launches, that data should be displayed first, then (in parallel) new data should be fetched from the backend.

### Users list
- [x] **Requirement 1**: Github users list can be obtained from https://api.github.com/users?since=0 in JSON format.
- [ ] **Requirement 2**: The list must support pagination (scroll to load more) utilizing since parameter as the integer ID of the last User loaded.
- [ ] **Requirement 3**: Page size has to be dynamically determined after the first batch is loaded.
- [ ] **Requirement 4**: The list has to display a spinner while loading data as the last list item.
- [x] **Requirement 5**: Every fourth avatar's (the image - not the background!) colour should have its colours inverted.
- [x] **Requirement 6**: List item view should have a note icon if there is note information saved for the given user.
- [ ] **Requirement 7**: Users list has to be searchable - local search only; in search mode, there is no pagination; username and note (see Profile section) fields should
be used when searching, precise match as well as contains should be used.

### Profile
- [x] **Requirement 1**: Profile info can be obtained from https://api.github.com/users/[username] in JSON format (e.g. https://api.github.com/users/tawk)
- [x] **Requirement 2**: The view should have the user's avatar as a header view followed by information fields (UIX is up to you).
- [x] **Requirement 3**: The section must have the possibility to retrieve and save back to the database the Note data (not available in GitHub api; local database only).
