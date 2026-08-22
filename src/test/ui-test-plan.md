# UI Test Plan

## Add, complete, and list a task
**Aim:** Verify that Rocky stores a task, marks it as done, and shows its completed status in the list.

**Input:**
```text
read book
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
added: read book, but what it mean?
____________________________________________________________
____________________________________________________________
Nice! Rocky marked this task as done:
[X] read book
____________________________________________________________
____________________________________________________________
1. [X] read book
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
