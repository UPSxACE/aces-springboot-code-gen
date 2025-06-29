@startuml
!theme cerulean
title Generate code from SQL

actor User as u
participant Api as a
participant Parser as p

u -> a: Submit SQL text
activate a
a -> p: Parse SQL & generate code
activate p
p -> p: Parse SQL statements
p -> p: Create table relationship graph
p -> p: Generate entity code from graph
p -> p: Generate repository code from graph
p --> a: Return generated code
deactivate p
a --> u: Return generated code response
deactivate a

@enduml