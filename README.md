
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
  - get the test report from the job (maybe use the created artefactory)
  - find a way to populate test table with the executed tests
- add a way to restart test(put a button in the for each test suite) (this way if some1 wants to rerun 
the job can do it with the button and if he wants to run a brand-new pipeline he can create a new test suite)
- add button in test suite with link to the gitlab job/pipeline
