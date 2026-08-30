# UI Test Plan

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

## Start with no saved tasks
**Aim:** Verify that Rocky starts with an empty list when no task file exists.

**Input:**
```text
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
Rocky remember you have these tasks
____________________________________________________________
Rocky don't see anything!
____________________________________________________________
Bye. We meet again soon!
____________________________________________________________
```

## Reject an invalid date
**Aim:** Verify that Rocky rejects a deadline with a date outside the yyyy-MM-dd format without adding it.

**Input:**
```text
deadline return book /by not-a-date
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
Use yyyy-MM-dd or yyyy-MM-dd HHmm, for example: 2019-10-15 or 2019-12-02 1800
Rocky remember you have these tasks
____________________________________________________________
Rocky don't see anything!
____________________________________________________________
Bye. We meet again soon!
____________________________________________________________
```

## Add, complete, and list different task types
**Aim:** Verify that Rocky creates to-do, deadline, and event tasks, marks a task as done, and displays each task through its type-specific format.

**Input:**
```text
todo read book
deadline return book /by 2019-12-02 1800
event project meeting /from 2019-12-02 1800 /to 2019-12-02 2000
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
[D][ ] return book (by: Dec 02 2019, 6:00PM)
Rocky see 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Amaze! Rocky add this to task...:
[E][ ] project meeting (from: Dec 02 2019, 6:00PM to: Dec 02 2019, 8:00PM)
Rocky see 3 tasks in the list.
____________________________________________________________
____________________________________________________________
Nice! Rocky marked this task as done:
[T][X] read book
____________________________________________________________
Rocky remember you have these tasks
____________________________________________________________
1. [T][X] read book
2. [D][ ] return book (by: Dec 02 2019, 6:00PM)
3. [E][ ] project meeting (from: Dec 02 2019, 6:00PM to: Dec 02 2019, 8:00PM)
____________________________________________________________
Bye. We meet again soon!
____________________________________________________________
```

## Load saved tasks when Rocky starts
**Aim:** Verify that Rocky reconstructs saved task types and completion status when it starts again.

**Input:**
```text
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
Rocky remember you have these tasks
____________________________________________________________
1. [T][X] read book
2. [D][ ] return book (by: Dec 02 2019, 6:00PM)
3. [E][ ] project meeting (from: Dec 02 2019, 6:00PM to: Dec 02 2019, 8:00PM)
____________________________________________________________
Bye. We meet again soon!
____________________________________________________________
```
