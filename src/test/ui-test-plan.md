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
Got it. I've added this task:
[T][ ] read book
Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! Rocky marked this task as done:
[T][X] read book
____________________________________________________________
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
