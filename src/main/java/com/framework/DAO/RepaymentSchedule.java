package com.framework.DAO;

public class RepaymentSchedule {
    private String strNo;
    private String strDays;
    private String strDate;
    private String strPaidDate;
    private String strPrincipalDue;
    private String strBalanceOfLoan	;
    private String strInterest;
    private String strFees;
    private String strPenalties;
    private String strDue;
    private String strPaid;
    private String strInAdvance;
    private String strLate;
    private String strOutStanding;

    public RepaymentSchedule() {
    }

    public RepaymentSchedule(String strNo, String strDays, String strDate, String strPaidDate, String strPrincipalDue, String strBalanceOfLoan, String strInterest, String strFees, String strPenalties, String strDue, String strPaid, String strInAdvance, String strLate, String strOutStanding) {
        this.strNo = strNo;
        this.strDays = strDays;
        this.strDate = strDate;
        this.strPaidDate = strPaidDate;
        this.strPrincipalDue = strPrincipalDue;
        this.strBalanceOfLoan = strBalanceOfLoan;
        this.strInterest = strInterest;
        this.strFees = strFees;
        this.strPenalties = strPenalties;
        this.strDue = strDue;
        this.strPaid = strPaid;
        this.strInAdvance = strInAdvance;
        this.strLate = strLate;
        this.strOutStanding = strOutStanding;
    }

    public String getStrNo() {
        return strNo;
    }

    public void setStrNo(String strNo) {
        this.strNo = strNo;
    }

    public String getStrDays() {
        return strDays;
    }

    public void setStrDays(String strDays) {
        this.strDays = strDays;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrPaidDate() {
        return strPaidDate;
    }

    public void setStrPaidDate(String strPaidDate) {
        this.strPaidDate = strPaidDate;
    }

    public String getStrPrincipalDue() {
        return strPrincipalDue;
    }

    public void setStrPrincipalDue(String strPrincipalDue) {
        this.strPrincipalDue = strPrincipalDue;
    }

    public String getStrBalanceOfLoan() {
        return strBalanceOfLoan;
    }

    public void setStrBalanceOfLoan(String strBalanceOfLoan) {
        this.strBalanceOfLoan = strBalanceOfLoan;
    }

    public String getStrInterest() {
        return strInterest;
    }

    public void setStrInterest(String strInterest) {
        this.strInterest = strInterest;
    }

    public String getStrFees() {
        return strFees;
    }

    public void setStrFees(String strFees) {
        this.strFees = strFees;
    }

    public String getStrPenalties() {
        return strPenalties;
    }

    public void setStrPenalties(String strPenalties) {
        this.strPenalties = strPenalties;
    }

    public String getStrDue() {
        return strDue;
    }

    public void setStrDue(String strDue) {
        this.strDue = strDue;
    }

    public String getStrPaid() {
        return strPaid;
    }

    public void setStrPaid(String strPaid) {
        this.strPaid = strPaid;
    }

    public String getStrInAdvance() {
        return strInAdvance;
    }

    public void setStrInAdvance(String strInAdvance) {
        this.strInAdvance = strInAdvance;
    }

    public String getStrLate() {
        return strLate;
    }

    public void setStrLate(String strLate) {
        this.strLate = strLate;
    }

    public String getStrOutStanding() {
        return strOutStanding;
    }

    public void setStrOutStanding(String strOutStanding) {
        this.strOutStanding = strOutStanding;
    }
}
