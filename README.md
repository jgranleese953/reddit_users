# reddit_users

This application was built using:

Kotlin 2.3.21
Android Gradle Plugin 9.2.0
Gradle: 9.4.1

Targets: Android 36 
Minimum: Android 24

This application was built using MVVM architecture and makes use of the following libraries:

Dependency Injection : Dagger Hilt (2.59.2)
Data Persistence: Android Room (2.8.4)
Networking: Retrofit (3.0.0) + OkHTTP Logging (5.3.2)
UI: Compose (2026.04.01) + Material (1.10.0)
Unit Testing: MockK (1.14.9) + Coroutines Test (1.10.2)

Decisions:

When first accessing the specs for this application, I made the decision to rely on the API as the 'one source of truth', 
while tempted to retrieve the API at the launch of the application and then store it within a Room database (those treating that as the 'one source of truth') 
I decided against it as the more the app might scale up, the more unfeasible it would be exponentially increase the database size.

I also created a new library (ImageLoader) as I was not permitted to use any third party libraries. Obviously in the real world, I would argue for the use of
a third party library such as Coil or Glide as this is 'reinventing the wheel' and both are seen as industry standards by Google.

I was also tempted to implement features beyond the scope of the spec, such as different views for "Home" and "Favourites" and 'Pull to Refresh' but didn't want to introduce scope creep 
to the application without first discussing with the product owner.

I made use of AI only for the process of investigating new features within Android Flow and Coroutines and also for the migration of AGP 9+ as ksp is (finally) 
the desired compiler but unfortunately Room has yet to catch up from kapt. For the purposes of transparency, I also used this project as a chance to learn more and play about 
new features offered for Android developers for my own personal development.