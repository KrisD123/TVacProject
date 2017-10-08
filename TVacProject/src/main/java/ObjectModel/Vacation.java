package ObjectModel;

/**
 * Created by kdodonov on 06.10.2017.
 */
public class Vacation {
private String vacationType;
private String vacationStart;
private String vacationEnd;
private int amountOfDays;
private String status;

    public String getVacationType() {
        return vacationType;
    }

    public String getVacationStart() {
        return vacationStart;
    }

    public String getVacationEnd() {
        return vacationEnd;
    }

    public int getAmountOfDays() {
        return amountOfDays;
    }

    public String getStatus() {
        return status;
    }

    public void setVacationType(String vacationType) {
        this.vacationType = vacationType;
    }

    public void setVacationStart(String vacationStart) {
        this.vacationStart = vacationStart;
    }

    public void setVacationEnd(String vacationEnd) {
        this.vacationEnd = vacationEnd;
    }

    public void setAmountOfDays(int amountOfDays) {
        this.amountOfDays = amountOfDays;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
