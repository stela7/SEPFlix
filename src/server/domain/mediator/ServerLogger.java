package server.domain.mediator;

import javafx.stage.FileChooser;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import server.Main;
import server.domain.model.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

/**
 * Created by aykon on 24-May-17.
 */
public class ServerLogger extends Observable {

    private final ArrayList<Log> actionsLog;

    public ServerLogger() {
        this.actionsLog = new ArrayList<>();
    }

    public synchronized void addAction(Log log) {
        actionsLog.add(log);
        super.setChanged();
        super.notifyObservers(this.actionsLog);
    }

    public ArrayList<Log> getActionsLog() {
        return actionsLog;
    }

    public void saveLog() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Log");
            //Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel file (*.xls)", "*.xls");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog and create file
            File file = fileChooser.showSaveDialog(Main.stage);
            if (file != null) {
                //create xls file and sheet
                WritableWorkbook workbook = null;
                workbook = Workbook.createWorkbook(file);
                WritableSheet sheet = workbook.createSheet("Log", 0);

                //format columns
                CellView columnView1 = sheet.getColumnView(0);
                CellView columnView2 = sheet.getColumnView(1);
                CellView columnView3 = sheet.getColumnView(2);
                CellView columnView4 = sheet.getColumnView(3);
                columnView1.setAutosize(true);
                columnView2.setAutosize(true);
                columnView3.setAutosize(true);
                columnView4.setAutosize(true);
                sheet.setColumnView(0, columnView1);
                sheet.setColumnView(1, columnView2);
                sheet.setColumnView(2, columnView3);
                sheet.setColumnView(3, columnView4);

                //cell formats
                WritableFont headFont = new WritableFont(WritableFont.ARIAL,
                        WritableFont.DEFAULT_POINT_SIZE,
                        WritableFont.BOLD,
                        false
                );

                WritableCellFormat head = new WritableCellFormat(headFont);
                head.setBackground(Colour.GRAY_50);
                head.setAlignment(Alignment.CENTRE);
                head.setVerticalAlignment(VerticalAlignment.CENTRE);

                WritableCellFormat redBackground = new WritableCellFormat();
                redBackground.setBackground(Colour.RED);

                //head of table
                Label headLabelIp = new Label(0, 0, "IP", head);
                Label headLabelAction = new Label(1, 0, "Action", head);
                Label headLabelLogged = new Label(2, 0, "Logged In", head);
                Label headLabelTime = new Label(3, 0, "Time", head);
                sheet.addCell(headLabelIp);
                sheet.addCell(headLabelAction);
                sheet.addCell(headLabelLogged);
                sheet.addCell(headLabelTime);

                //data
                Label labelIp;
                Label labelAction;
                Label labelLogged;
                Label labelTime;
                for (int i = 0; i < actionsLog.size(); i++) {
                    Date date = actionsLog.get(i).getDate();
                    if (actionsLog.get(i).getAction().equals("Alert")) {
                        String action;
                        if (actionsLog.get(i).getMessage() == null) {

                            action = actionsLog.get(i).getAction();
                        } else {
                            action = actionsLog.get(i).getAction() + " - " + actionsLog.get(i).getMessage();
                        }
                        labelIp = new Label(0, i + 1, actionsLog.get(i).getIp(), redBackground);
                        labelAction = new Label(1, i + 1, action, redBackground);
                        labelLogged = new Label(2, i + 1, (actionsLog.get(i).getLoggedIn()) ? "Yes" : "No", redBackground);
                        labelTime = new Label(3, i + 1, date.toString(), redBackground);
                    } else {
                        labelIp = new Label(0, i + 1, actionsLog.get(i).getIp());
                        labelAction = new Label(1, i + 1, actionsLog.get(i).getAction());
                        labelLogged = new Label(2, i + 1, (actionsLog.get(i).getLoggedIn()) ? "Yes" : "No");
                        labelTime = new Label(3, i + 1, date.toString());
                    }
                    sheet.addCell(labelIp);
                    sheet.addCell(labelAction);
                    sheet.addCell(labelLogged);
                    sheet.addCell(labelTime);
                }
                workbook.write();
                workbook.close();
            }
        } catch (WriteException | IOException e) {
            e.printStackTrace();
        }

    }
}
