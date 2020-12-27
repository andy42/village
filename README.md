# co.uk.genesisengineers.village

git clone https://github.com/andy42/village.git

open IntelliJ idea
File->open
select village

in IntelliJ idea open build.gradle
find task "task runApt(type:Exec)""
click the green arrow to the left of the name

Add Configuration (Run/Debug Configuration)
press add cross symbol in the top left of the Run/Debug Configuration window
select Application from the dropdown
give the Configuration name
set the values for
Main class: co.uk.genesisengineers.village.AppKt
use classpath of module: village_main