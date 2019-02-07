package com.opengm.view

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TreeItem
import javafx.scene.control.cell.TextFieldTreeCell
import javafx.scene.input.ClipboardContent
import javafx.scene.input.DataFormat
import javafx.scene.input.TransferMode
import tornadofx.*
import java.util.*
import com.opengm.view.HREntity.Department
import com.opengm.view.HREntity.Person

sealed class HREntity(val name: String) {
    val id = UUID.randomUUID()

    override fun toString() = name

    class Person(name: String) : HREntity(name)

    class Department(name: String, people: List<Person>? = emptyList()) : HREntity(name) {
        val people: ObservableList<HREntity> = FXCollections.observableArrayList<HREntity>(people)
    }
}

val departments = listOf(
        Department("Marketing",
                listOf(
                        Person("Mary Hanes"),
                        Person("Erin James")
                )
        ),
        Department("Customer Service",
                listOf(
                        Person("Steve Folley"),
                        Person("Erlick Foyes"),
                        Person("Larry Cable")
                )
        ),
        Department("Help Desk",
                listOf(
                        Person("John Ramsey"),
                        Person("Jacob Mays")
                )
        )
).observable()

class DragAndDropView : View() {
    companion object {
        val DATA_FORMAT_PERSON_ID = DataFormat("PersonId")
        val DATA_FORMAT_INDEX = DataFormat("Index")

        const val DROP_HINT_STYLE = "-fx-border-color: #eea82f; -fx-border-width: 2 0 0 0; -fx-padding: 3 3 1 3"
    }

    override val root = treeview<HREntity> {
        // Create root item
        root = TreeItem(Department("Departments"))

        isShowRoot = false

        // Root is departments, else list children of the current Person
        populate { parent ->
            if (parent == root) departments else (parent.value as? Department)?.people
        }

        setCellFactory {
            object : TextFieldTreeCell<HREntity>() {
                init {
                    setOnDragDetected { event ->
                        if (item is Person) {
                            val db = startDragAndDrop(TransferMode.MOVE)
                            val content = ClipboardContent()
                            content[DATA_FORMAT_PERSON_ID] = item.id
                            content[DATA_FORMAT_INDEX] = index
                            db.setContent(content)
                            db.dragView = snapshot(null, null)
                            event.consume()
                        }
                    }
                    setOnDragOver { event ->
                        if (event.dragboard.hasContent(DATA_FORMAT_PERSON_ID)) {
                            event.acceptTransferModes(TransferMode.MOVE)
                        }
                    }
                    setOnDragEntered { event ->
                        event.isAccepted
                        style = DROP_HINT_STYLE
                    }
                    setOnDragExited { _ ->
                        style = ""
                    }
                    setOnDragDropped { event ->
                        if (event.dragboard.hasContent(DATA_FORMAT_PERSON_ID)) {
                                val sourceIndex = event.dragboard.getContent(DATA_FORMAT_INDEX) as Int
                                val dragItem = getTreeItem(sourceIndex) as TreeItem<Person>

                                val person = dragItem.value
                                val sourceDepartment = dragItem.parent.value as Department

                            try {
                                if (treeItem.parent != root) {
                                    val targetDepartment = treeItem.parent.value as Department
                                    //println(targetDepartment)

                                    val targetItem = getTreeItem(index) as TreeItem<Person>
                                    val targetIndex = targetItem.parent.children.indexOf(targetItem)

                                    sourceDepartment.people.remove(person)
                                    targetDepartment.people.add(targetIndex, person)
                                } else {
                                    val targetDepartment = treeItem.previousSibling().value as Department
                                    println(targetDepartment)

                                    sourceDepartment.people.remove(person)
                                    targetDepartment.people.add(person)
                                }
                            }
                            catch (e: Exception) {
                                val targetDepartment = root.children.get(root.children.size-1).value as Department
                                println(targetDepartment)

                                sourceDepartment.people.remove(person)
                                targetDepartment.people.add(person)
                            }

                            event.isDropCompleted = true
                        }
                    }
                }
            }
        }
    }
}
