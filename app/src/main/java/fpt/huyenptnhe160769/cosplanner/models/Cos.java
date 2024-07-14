package fpt.huyenptnhe160769.cosplanner.models;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Cos {
    private double budget = 0.0d;
    private Calendar dueDate = null;
    private Calendar endDate = null;
    private long id = 0;
    private Calendar initDate = null;
    private String name = "";
    private String notes = "";

//    private int numberOfEvents = 0;

    private int numberOfPhotos = 0;

//    private int numberOfPhotoshoots = 0;
//    private int numberOfPrizes = 0;

    private int rTimeDD = 0;
    private int rTimeMM = 0;
    private int rTimeYY = 0;
    private String series = "";
    private int status = 1;

//    private int tasksPending = 0;
//    private int tasksReady = 0;
//    private int tasksTotal = 0;

    private int timeDD = 0;
    private int timeHRS = 0;
    private int timeMIN = 0;
    private int timeMM = 0;
    private int timeYY;
    private double totalCost = 0.0d;
    private double totalPercent = 0.0d;
    private double totalPercentBuy = 0.0d;

//    private double totalPercentMake = 0.0d;

    public void setId(long id2) {
        this.id = id2;
    }

    public void setName(String s) {
        this.name = s;
    }

    public void setSeries(String s) {
        this.series = s;
    }

    public void setStatus(int s) {
        this.status = s;
    }

    public void setInitDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(1, year);
        c.set(2, month - 1);
        c.set(5, day);
        this.initDate = c;
    }

    public void setEndDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(1, year);
        c.set(2, month - 1);
        c.set(5, day);
        this.endDate = c;
    }

    public void setTimeDD(int dd) {
        this.timeDD = dd;
    }

    public void setTimeMM(int mm) {
        this.timeMM = mm;
    }

    public void setTimeYY(int yy) {
        this.timeYY = yy;
    }

    public void setTimeHRS(int hrs) {
        this.timeHRS = hrs;
    }

    public void setTimeMIN(int min) {
        this.timeMIN = min;
    }

    public void setTotalPercent(double f) {
        this.totalPercent = f;
    }

    public void setTotalPercentBuy(double f) {
        this.totalPercentBuy = f;
    }

//    public void setTotalPercentMake(double f) {
//        this.totalPercentMake = f;
//    }

    public void setTotalCost(double f) {
        this.totalCost = f;
    }

//    public void setNumberOfEvents(int i) {
//        this.numberOfEvents = i;
//    }
//
//    public void setNumberOfPhotoshoots(int i) {
//        this.numberOfPhotoshoots = i;
//    }

    public void setNumberOfPhotos(int i) {
        this.numberOfPhotos = i;
    }

//    public void setNumberOfPrizes(int i) {
//        this.numberOfPrizes = i;
//    }

    public void setNotes(String s) {
        this.notes = s;
    }

    public void setBudget(double d) {
        this.budget = d;
    }

    public void setDueDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(1, year);
        c.set(2, month - 1);
        c.set(5, day);
        this.dueDate = c;
    }

    public void setDueDateNull() {
        this.dueDate = null;
    }

    public void setRTimeDD(int dd) {
        this.rTimeDD = dd;
    }

    public void setRTimeMM(int mm) {
        this.rTimeMM = mm;
    }

    public void setRTimeYY(int yy) {
        this.rTimeYY = yy;
    }

//    public void setTasksPending(int p) {
//        this.tasksPending = p;
//    }
//
//    public void setTasksReady(int r) {
//        this.tasksReady = r;
//    }
//
//    public void setTasksTotal(int t) {
//        this.tasksTotal = t;
//    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSeries() {
        return this.series;
    }

    public int getStatusForDB() {
        return this.status;
    }

    public boolean isPlanned() {
        return this.status == 0;
    }

    public boolean isInProcess() {
        return this.status == 1;
    }

    public boolean isComplete() {
        return this.status == 2;
    }

    public String getInitDateString() {
        if (this.initDate != null) {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this.initDate.getTime());
        }
        return "--";
    }

    public String getEndDateString() {
        if (this.endDate != null) {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this.endDate.getTime());
        }
        return "--";
    }

    public String getInitDateStringWithFormat(int format) {
        if (this.initDate != null) {
            return DateFormat.getDateInstance(format, Locale.getDefault()).format(this.initDate.getTime());
        }
        return "--";
    }

    public String getEndDateStringWithFormat(int format) {
        if (this.endDate != null) {
            return DateFormat.getDateInstance(format, Locale.getDefault()).format(this.endDate.getTime());
        }
        return "--";
    }

    public String getTotalPercentString() {
        return NumberFormat.getPercentInstance(Locale.getDefault()).format(this.totalPercent * 0.01d);
    }

    public String getTotalPercentBuyString() {
        return NumberFormat.getPercentInstance(Locale.getDefault()).format(this.totalPercentBuy * 0.01d);
    }

//    public String getTotalPercentMakeString() {
//        return NumberFormat.getPercentInstance(Locale.getDefault()).format(this.totalPercentMake * 0.01d);
//    }

    public Calendar getInitDate() {
        return this.initDate;
    }

    public Calendar getEndDate() {
        return this.endDate;
    }

    public int getTimeDD() {
        return this.timeDD;
    }

    public int getTimeMM() {
        return this.timeMM;
    }

    public int getTimeYY() {
        return this.timeYY;
    }

    public int getTimeHRS() {
        return this.timeHRS;
    }

    public int getTimeMIN() {
        return this.timeMIN;
    }

    public double getTotalPercent() {
        return this.totalPercent;
    }

    public double getTotalPercentBuy() {
        return this.totalPercentBuy;
    }

//    public double getTotalPercentMake() {
//        return this.totalPercentMake;
//    }

    public double getTotalCost() {
        return this.totalCost;
    }

//    public int getNumberOfEvents() {
//        return this.numberOfEvents;
//    }
//
//    public int getNumberOfPhotoshoots() {
//        return this.numberOfPhotoshoots;
//    }

    public int getNumberOfPhotos() {
        return this.numberOfPhotos;
    }

//    public int getNumberOfPrizes() {
//        return this.numberOfPrizes;
//    }

    public String getNotes() {
        return this.notes;
    }

    public double getBudget() {
        return this.budget;
    }

    public boolean hasInitDate() {
        return this.initDate != null;
    }

    public boolean hasDueDate() {
        return this.dueDate != null;
    }

    public boolean hasEndDate() {
        return this.endDate != null;
    }

    public String getDueDateString() {
        if (this.dueDate != null) {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(this.dueDate.getTime());
        }
        return "--";
    }

    public String getDueDateStringWithFormat(int format) {
        if (this.dueDate != null) {
            return DateFormat.getDateInstance(format, Locale.getDefault()).format(this.dueDate.getTime());
        }
        return "--";
    }

    public int getRTimeDD() {
        return this.rTimeDD;
    }

    public int getRTimeMM() {
        return this.rTimeMM;
    }

    public int getRTimeYY() {
        return this.rTimeYY;
    }

//    public int getTasksPending() {
//        return this.tasksPending;
//    }
//
//    public int getTasksReady() {
//        return this.tasksReady;
//    }
//
//    public int getTasksTotal() {
//        return this.tasksTotal;
//    }
}
