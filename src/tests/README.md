# Examples
Here are some tests files putting the `Update`, `Insert`, and `Select` classes in action!

## Update Results (With Debug)
```
Initializing an Update query
Update->executeQuery() - Compiling query with table 'test' and where clause`id`=2

Process finished with exit code 0
```

## Insert Results (With Debug)
```
Initializing an Insert query
Delete->executeQuery() - Compiling query with table 'test'

Process finished with exit code 0
```

## Select Results (With Debug)
```
Initializing a Select query
Select->executeQuery() - Compiling query with table 'test'

Rows from standard query:

ID - 25
Msg - This is message #3! I am not thanos!

ID - 28
Msg - This is message #6! Yah that last one wasn't #4


Rows from prepared query:

ID - 23
Msg - This is message #1! I am cool.

ID - 27
Msg - This is message #4! What lol

ID - 30
Msg - This is message #8


Process finished with exit code 0
```