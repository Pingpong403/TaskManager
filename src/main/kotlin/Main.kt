import java.io.File

fun main(args: Array<String>) {
    // Get list of users.
    val users = ArrayList<String>()
    val usersFilename = "users.txt"
    var usersFile = File(usersFilename)
    usersFile.forEachLine(action = {line -> users.add(line)})


    println("Welcome to the task manager!")
    var signedIn = false
    var username = ""
    while (!signedIn)
    {
        println("1- Log in")
        println("2- Register")
        var entry = readln()
        if (entry == "1")
        {
            print("Enter username: ")
            username = readln()
            if (username in users)
            {
                println("Welcome, ${username}!")
                signedIn = true
            }
            else
            {
                println("Sorry, but you don't seem to be registered.")
            }
        }
        else if (entry == "2")
        {
            print("Register username: ")
            username = readln()
            if (username in users)
            {
                println("It looks like you're already registered!")
            }
            else
            {
                if (username == "")
                {
                    println("Please enter a valid username.")
                }
                else
                {
                    usersFile.appendText("${username}\n")
                    println("You're all registered, ${username}!")
                    signedIn = true
                }
            }
        }
        else
        {
            println("Please enter a valid option.")
        }
    }

    var action : String
    var quit = false
    do
    {
        println("1- Enter tasks")
        println("2- Get tasks")
        println("3- Finish tasks")
        println("4- Quit task manager")
        action = readln()
        when (action)
        {
            "1" -> enterTasks(username)
            "2" -> getTasks(username)
            "3" -> finishTasks(username)
            "4" -> quit = true
            else -> println("Please enter a valid option.")
        }
    } while (!quit)
    println("Get those tasks done!")
}

fun enterTasks(username : String)
{
    // Instantiate file
    val filename = "${username}.txt"
    val file = File(filename)
    // Create file if it doesn't exist
    file.createNewFile()

    // Get tasks
    println("Enter as many tasks as you want.")
    println("Type \"done\" to exit and review/edit tasks")
    var taskName: String
    var done = false
    do
    {
        print("Task name: ")
        taskName = readln()
        if (taskName == "done")
        {
            done = true
        }
        else
        {
            var task = Task(taskName)
            file.appendText("${task.getString()}\n")
            println()
        }
    } while (!done)
    println()
}

fun getTasks(username : String)
{
    val filename = "${username}.txt"
    val file = File(filename)

    try
    {
        var rawData = file.readText()
        var splitByLine = rawData.split("\n")

        for (line : String in splitByLine)
        {
            var lineSplit = line.split(":")
            if (lineSplit.size != 1)
            {
                print("Name: ${lineSplit[0]}\nDescription:\n${lineSplit[1]}\n\n")
            }
        }

        println("You have ${splitByLine.size - 1} task${if (splitByLine.size - 1 != 1) "s" else ""} to do.\n")
    }
    catch (FileNotFoundException : Exception)
    {
        println("You don't have any tasks yet!\n")
    }
}

fun finishTasks(username : String)
{
    val filename = "${username}.txt"
    val file = File(filename)

    try
    {
        var done = false
        do
        {
            var rawData = file.readText()
            var splitByLine = rawData.split("\n")

            if (splitByLine.size != 1)
            {
                println("Your tasks:")
                for (i : Int in 0..splitByLine.size - 1)
                {
                    var lineSplit = splitByLine[i].split(":")
                    var taskName = lineSplit[0]
                    if (lineSplit.size != 1)
                    {
                        println("${i + 1}- $taskName")
                    }
                }
                println()
                print("Enter the number for the task you have completed.\nType \"0\" when finished: ")
                var taskDone = readln()
                if (taskDone.toInt() < 0 || taskDone.toInt() >= splitByLine.size)
                {
                    println("Please enter a valid number.")
                }
                else if (taskDone != "0")
                {
                    println("You have completed task \"${splitByLine[taskDone.toInt() - 1].split(":")[0]}\"? y/n: ")
                    var completed = readln()
                    if (completed == "y")
                    {
                        var updatedList = ArrayList<String>()
                        for (line : String in splitByLine)
                        {
                            updatedList.add(line)
                        }
                        updatedList.removeAt(taskDone.toInt() - 1)
                        rawData = updatedList.joinToString("\n")
                        file.writeText(rawData)
                    }
                    else
                    {
                        println("Get that task done!")
                    }
                }
                else
                {
                    done = true
                }
            }
            else
            {
                println("You don't have any tasks to complete! Congrats!\n")
                done = true
            }
        } while (!done)
    }
    catch (FileNotFoundException : Exception)
    {
        println("You don't have any tasks yet!\n")
    }
}

class Task(private var name : String)
{
    private var desc : String = ""

    init
    {
        println("Task created successfully.\nStart typing description:\n")
        getDescription()
    }

    private fun getDescription()
    {
        desc = readln()
        if (desc.isEmpty())
        {
            print("No description? y/n: ")
            if (readln() != "y")
            {
                getDescription()
            }
        }
    }

    fun getString() : String
    {
        return "${name}:${desc}"
    }
}