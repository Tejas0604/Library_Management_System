package com.example.librarymanagement;

public class Booking {

    private String studentName;
    private String mobileNumber;
    private String comingTime;
    private String goTime;
    private String membershipPlan;
    private String seatNumber;
    private String labName;
    private String startDate;
    private String endDate;
    private int remainingDays;
    private String paymentStatus;
    private String labId; // ✅ Added field

    public Booking() {
        // Required empty constructor for Firebase
    }

    public Booking(String studentName, String mobileNumber, String comingTime, String goTime,
                   String membershipPlan, String seatNumber, String labName,
                   String startDate, String endDate, int remainingDays) {
        this.studentName = studentName;
        this.mobileNumber = mobileNumber;
        this.comingTime = comingTime;
        this.goTime = goTime;
        this.membershipPlan = membershipPlan;
        this.seatNumber = seatNumber;
        this.labName = labName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingDays = remainingDays;
        this.paymentStatus = "unpaid"; // Default value
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getComingTime() {
        return comingTime;
    }

    public void setComingTime(String comingTime) {
        this.comingTime = comingTime;
    }

    public String getGoTime() {
        return goTime;
    }

    public void setGoTime(String goTime) {
        this.goTime = goTime;
    }

    public String getMembershipPlan() {
        return membershipPlan;
    }

    public void setMembershipPlan(String membershipPlan) {
        this.membershipPlan = membershipPlan;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // ✅ Added getter and setter for labId
    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }
}
