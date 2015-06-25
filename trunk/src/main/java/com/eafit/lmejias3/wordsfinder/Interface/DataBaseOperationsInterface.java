package com.eafit.lmejias3.wordsfinder.Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.eafit.lmejias3.wordsfinder.DataBase.DataBaseManager;

/**
 * Interface for the user to add or remove rows to the selected DB
 * @see JFrame
 * @see ActionListener
 */
public class DataBaseOperationsInterface extends JFrame
  implements ActionListener {

  //Conection to the database selected by the user
  private DataBaseManager database;

  //Words
  JComboBox words;
  JTextField newWord;

  //Labels
  JComboBox labels;
  JTextField newLabel;

  //Buttons
  JButton eraseButton, saveButton;

  /**
   * Constructor of the Interface with a GridLayout
   * @param database Reference to a DataBaseManager instance
   * @see GridLayout
   * @see DataBaseManager
   */
  public DataBaseOperationsInterface (DataBaseManager database) {
    this.database = database;

    //Configure the interface with a GribLayout ---------------------------
    setTitle("DATABASE OPERATIONS");
    setSize(400, 150);
    setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    setLayout(new GridLayout(4,3));
    //---------------------------------------------------------------------

    //Add components ------------------------------------------------------
    //Row 1
    add(new JLabel("Word"));
    add(new JLabel());
    add(new JLabel("Label"));

    //Row 2
    words = new JComboBox();
    add(words);

    add(new JLabel());

    labels = new JComboBox();
    add(labels);

    updateComboBoxes();

    //Row 3
    newWord = new JTextField("Write the new word");
    newWord.setEditable(true);
    add(newWord);

    add(new JLabel());

    newLabel = new JTextField("Write the new label");
    newLabel.setEditable(true);
    add(newLabel);

    //Row 4
    add(new JLabel());

    eraseButton = new JButton("Erase");
    eraseButton.setActionCommand("Delete");
    eraseButton.addActionListener(this);
    eraseButton.setEnabled(false);
    add(eraseButton);

    saveButton = new JButton("Save");
    saveButton.setActionCommand("Add");
    saveButton.addActionListener(this);
    add(saveButton);
    //---------------------------------------------------------------------

    //Add Actions listeners to JComboBoxes --------------------------------
    //words
    ActionListener wac = new ActionListener() {
        //add actionlistner to detect when the user select the field "New"
        @Override
        public void actionPerformed(ActionEvent e) {

          if (words.getSelectedItem() != null) {

            //get the user's selected item
            String s = words.getSelectedItem().toString();

            if (s.equals("New")) {
              //If the slected field is "New"
              //Activate the newWord text field
              //for the user write the new word
              //and desactivate the Button 'Erase'
              newWord.setEditable(true);
              eraseButton.setEnabled(false);
            } else {
              //If not, desactivate the text field and activate the button
              newWord.setEditable(true);
              eraseButton.setEnabled(true);
            }
          }
        }
      };
    words.addActionListener(wac);

    //Labels
    ActionListener lac = new ActionListener() {
        //add actionlistner to detect when the user select the field "New"
        @Override
        public void actionPerformed(ActionEvent e) {

          if (labels.getSelectedItem() != null) {
            //get the user's selected item
            String s = labels.getSelectedItem().toString();

            if (s.equals("New")) {
              //If the slected field is "New"
              //Activate the newLabel text field
              //for the user write the new label
              //and desactivate the Button 'Erase'
              newLabel.setEditable(true);
              eraseButton.setEnabled(false);
            } else {
              //If not, desactivate the text field
              newLabel.setEditable(false);
            }
          }
        }
      };
    labels.addActionListener(lac);
    //--------------------------------------------------------------------
  }

  /**
   * Here are handled all the events activated in this interface
   * @see ActionEvent
   */
  @Override
  public void actionPerformed(ActionEvent evt) {
    String Action = evt.getActionCommand();
    switch (Action){
    case "Add":
      addData();
      break;
    case "Delete":
      deleteData();
      break;
    }

    updateComboBoxes();
  }

  /**
   * Add the information to the database
   */
  private void addData () {
    String word = "", column = "", data = "";

    if (words.getSelectedItem().toString().equals("New")) {
      word = newWord.getText();
    } else {
      word = words.getSelectedItem().toString();
    }

    switch (labels.getSelectedItem().toString()) {
    case "New":
      column = "Label";
      data = newLabel.getText();
      break;
    case "Find":
      column = "Find";
      data = "TRUE";
      break;
    case "Excluded":
      column = "Excluded";
      data = "TRUE";
      break;
    default:
      column = "Label";
      data = labels.getSelectedItem().toString();
      break;
    }

    database.updateData(word, column, data);
  }

  /**
   * Erase data from the database
   */
  private void deleteData () {
    String word = "", column = "";

    word = words.getSelectedItem().toString();

    switch (labels.getSelectedItem().toString()) {
    case "Find":
      column = "Find";
      break;
    case "Excluded":
      column = "Excluded";
      break;
    default:
      column = "Label";
      break;
    }

    database.updateData(word, column, "FALSE");
  }

  /**
   * Update the values of the Combo boxes
   */
  private void updateComboBoxes () {
    //Update words
    words.removeAllItems();
    words.addItem("New");
    for (String item : database.getDifferent("Word")) words.addItem(item);

    //Update Labels
    labels.removeAllItems();
    labels.addItem("New");
    labels.addItem("Excluded");
    labels.addItem("Find");
    for (String item : database.getDifferent("Label")) labels.addItem(item);
  }
}
