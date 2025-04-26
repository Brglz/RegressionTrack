
TODO:

- add authentication
  - make it with roles ADMIN/USER
  - make a view for changing roles of users (only admins can see it and use it)
  - make the login like hirehub
- make gitlab integration
  - authentication
  - map data
  - run regression from the app
  - update the data in the app when the gitlab job is ready ([MAY] send notification through slack/email/other
 when the job is ready)
  - test pipeline creation
  - test job creation
  - test job restart
  - get the test report from the job (maybe use the created artifactory)
  - find a way to populate test table with the executed tests
