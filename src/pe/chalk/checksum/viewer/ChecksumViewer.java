/*
 * Copyright 2015 ChalkPE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pe.chalk.checksum.viewer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ChalkPE <chalkpe@gmail.com>
 * @since 2015-05-31
 */
public class ChecksumViewer extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    @SuppressWarnings("CssInvalidPropertyValue")
    public void start(Stage stage) throws Exception{
        stage.setWidth(700);
        stage.setHeight(400);

        stage.setMinWidth(700);
        stage.setMinHeight(400);

        stage.setTitle("Checksum Viewer");
        stage.setOnCloseRequest(value -> System.exit(0));

        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Roboto");

        ComboBox<String> algorithm = new ComboBox<>();
        algorithm.setEditable(true);
        algorithm.getItems().addAll("MD5", "SHA-1", "SHA-256");
        algorithm.getSelectionModel().selectLast();

        TextArea input = new TextArea("Copyright 2015 ChalkPE\n\nLicensed under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance with the License.\nYou may obtain a copy of the License at\n\n    http://www.apache.org/licenses/LICENSE-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\nSee the License for the specific language governing permissions and\nlimitations under the License.");
        TextField output = new TextField("http://chalk.pe/");

        input.setWrapText(true);

        input.setEditable(true);
        output.setEditable(false);

        algorithm.setStyle("-fx-font-family: 'Roboto'");
        input.setStyle("-fx-font-family: 'Roboto'");
        output.setStyle("-fx-font-family: 'Roboto'");

        ChangeListener<String> textListener = (observable, oldValue, newValue) -> output.setText(getCipher(algorithm.getValue(), newValue));

        algorithm.valueProperty().addListener(textListener);
        input.textProperty().addListener(textListener);

        output.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                ClipboardContent content = new ClipboardContent();
                content.putString(output.getText());

                Clipboard.getSystemClipboard().setContent(content);
            }
        });

        BorderPane root = new BorderPane();

        root.setTop(algorithm);
        root.setCenter(input);
        root.setBottom(output);

        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }

    public static String getCipher(String algorithm, String plaintext){
        try{
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(plaintext.getBytes());

            StringBuilder result = new StringBuilder();
            for(byte b: digest.digest()) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

            return result.toString();
        }catch(NoSuchAlgorithmException e){
            return "[ERROR] No such algorithm!";
        }
    }
}
