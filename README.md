# co.uk.genesisengineers.village

## project setup

open cmd and cd to IdeaProjects dir
then run
```
git clone https://github.com/andy42/village.git

cd village
gradle build --refresh-dependencies
gradlew runApt
```
gradle build --refresh-dependencies might fail but just carry on

open IntelliJ idea
File->open
select village


### Add Configuration (Run/Debug Configuration)
press add cross symbol in the top left of the Run/Debug Configuration window
select Application from the dropdown
give the Configuration name
set the values for
Main class: co.uk.genesisengineers.village.AppKt
use classpath of module: village_main


## update resources
game assets are stored in the src/main/res folder
files in these folders are then passed and copied to src/main/resources folder
do not change files in the resources folder, as any changes will be over overridden

each file in Res is then added to the R class in  build/generated/kotlin/R.kt
you can the use this class to refrence all files in the res folder
e.g. R.drawables.backArrow_json
this will resolve to an integer that maps to the files details in src/main/resources/assets.json

to run the task that will pass the files in res folder run in the root project
```
gradlew runApt
```
 or

in IntelliJ idea open build.gradle
find task "task runApt(type:Exec)""
click the green arrow to the left of the name



