# UI Test Plan

## Add, complete, and list different task types
**Aim:** Verify that Rocky creates to-do, deadline, and event tasks, marks a task as done, and displays each task through its type-specific format.

**Input:**
```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
list
bye
```

**Expected output:**
```text
____________________________________________________________
 ____             _          
|  _ \ ___   ___| | ___   _ 
| |_) / _ \ / __| |/ / | | |
|  _ < (_) | (__|   <| |_| |
|_| \_\___/ \___|_|\_\__, |
                         |___/

Hello! I Rocky.
Amaze, what a special human being! What rocky do for you?
____________________________________________________________
____________________________________________________________
Amaze! Rocky add this to task...:
[T][ ] read book
Rocky see 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Amaze! Rocky add this to task...:
[D][ ] return book (by: Sunday)
Rocky see 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Amaze! Rocky add this to task...:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Rocky see 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! Rocky marked this task as done:
[T][X] read book
____________________________________________________________
Rocky remember you have these tasks
____________________________________________________________
1. [T][X] read book
2. [D][ ] return book (by: Sunday)
3. [E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
Bye. We meet again soon!
____________________________________________________________
```

## Reject an invalid task number
**Aim:** Verify that Rocky handles a mark command for a task that does not exist without adding a task or crashing.

**Input:**
```text
mark 1
bye
```

**Expected output:**
```text
____________________________________________________________
 ____             _          
|  _ \ ___   ___| | ___   _ 
| |_) / _ \ / __| |/ / | | |
|  _ < (_) | (__|   <| |_| |
|_| \_\___/ \___|_|\_\__, |
                         |___/

Hello! I Rocky.
Amaze, what a special human being! What rocky do for you?
____________________________________________________________
Rocky cannot find that task number.
Bye. We meet again soon!
____________________________________________________________
```

## Delete a task and list the remaining tasks
**Aim:** Verify that deleting an existing task updates the in-memory list and the displayed task list.

**Input:**
```text
todo first task
todo second task
delete 1
list
bye
```

**Expected output:**
```text
____________________________________________________________
 ____             _          
|  _ \ ___   ___| | ___   _ 
| |_) / _ \ / __| |/ / | | |
|  _ < (_) | (__|   <| |_| |
|_| \_\___/ \___|_|\_\__, |
                         |___/

Hello! I Rocky.
Amaze, what a special human being! What rocky do for you?
____________________________________________________________
____________________________________________________________
Amaze! Rocky add this to task...:
[T][ ] first task
Rocky see 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Amaze! Rocky add this to task...:
[T][ ] second task
Rocky see 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Rocky will remove that annoying task for you!:
[T][ ] first task
____________________________________________________________
Rocky remember you have these tasks
____________________________________________________________
1. [T][ ] second task
____________________________________________________________
Bye. We meet again soon!
____________________________________________________________
```
