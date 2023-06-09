package org.intelehealth.ezazi.ui.dialog;import android.content.Context;import android.content.res.Resources;import android.graphics.Typeface;import android.os.Build;import android.util.Log;import android.view.View;import android.widget.AdapterView;import android.widget.ArrayAdapter;import android.widget.DatePicker;import android.widget.Spinner;import android.widget.TextView;import androidx.annotation.RequiresApi;import org.intelehealth.ezazi.R;import org.intelehealth.ezazi.databinding.DialogDatePickerViewBinding;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Calendar;import java.util.Date;import java.util.Objects;/** * Created by Kaveri Zaware on 07-06-2023 * email - kaveri@intelehealth.org **/public class DatePickerDialog extends BaseDialogFragment<Void> implements DatePicker.OnDateChangedListener {    private DialogDatePickerViewBinding datePickerViewBinding;    private static final String TAG = "DatePickerDialog";    private DatePickerDialog.OnDateChangeListener listener;    Spinner spinnerMonths, spinnerYear;    ArrayAdapter<Integer> yearArrayAdapter;    ArrayAdapter<String> monthsArrayAdapter;    Calendar calendarInstanceMain;    int year, month, day;    Calendar calendarInstanceCurrent;    int currentYear;    int currentMonth;    @Override    public void onDismiss() {        super.onDismiss();    }    @Override    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {        int day1 = datePickerViewBinding.datePicker.getDayOfMonth();        int month1 = datePickerViewBinding.datePicker.getMonth();        int year1 = datePickerViewBinding.datePicker.getYear();        calendarInstanceMain.set(year1, month1, day1);        //set textview        String selectedMonthName = new SimpleDateFormat("MMM").format(calendarInstanceMain.getTime());        String selectedDate = dayOfMonth + " " + selectedMonthName + ", " + year;        datePickerViewBinding.tvSelectedDate.setText(selectedDate);    }    public interface OnDateChangeListener {        void onDateSelection(String date);    }    public void setListener(OnDateChangeListener listener) {        this.listener = listener;    }    @Override    public void onSubmit() {        if (listener != null) {            Log.d(TAG, "onSubmit: date final : " + datePickerViewBinding.tvSelectedDate.getText().toString());            listener.onDateSelection(datePickerViewBinding.tvSelectedDate.getText().toString());        }    }    @RequiresApi(api = Build.VERSION_CODES.O)    @Override    View getContentView() {        datePickerViewBinding = DialogDatePickerViewBinding.inflate(getLayoutInflater(), null, false);        //for selection of dates        calendarInstanceMain = Calendar.getInstance();        //for getting default date - year, month, date        calendarInstanceCurrent = Calendar.getInstance();        //get current year and month - default        currentYear = calendarInstanceCurrent.get(Calendar.YEAR);        currentMonth = calendarInstanceCurrent.get(Calendar.MONTH) + 1;        //init ui        spinnerMonths = datePickerViewBinding.spinnerMonthsCalviewNew;        spinnerYear = datePickerViewBinding.spinnerYearCalviewNew;        hideLayoutElements(datePickerViewBinding);        //fillup header dropdowns        fillMonthsSpinner();        fillYearSpinner(currentYear);        setupCurrentDate();        //set max date and min date - pass year as a parameter        //setMinDateForDatePicker(10);        //setMaxDateForDatePicker(12);        return datePickerViewBinding.getRoot();    }    @RequiresApi(api = Build.VERSION_CODES.O)    private void setupCurrentDate() {        Date cDate = new Date();        String currentDate = new SimpleDateFormat("dd MMM, yyyy").format(cDate);        datePickerViewBinding.tvSelectedDate.setText(currentDate);        datePickerViewBinding.datePicker.setOnDateChangedListener(this);        setupCalendar();        if (calendarInstanceMain != null) {            //update month spinner            SimpleDateFormat month_date = new SimpleDateFormat("MMM");            String month_name = month_date.format(calendarInstanceMain.getTime());            spinnerMonths.setSelection(monthsArrayAdapter.getPosition(month_name));            //update year spinner            spinnerYear.setSelection(yearArrayAdapter.getPosition(year));        }    }    private void setupCalendar() {        year = calendarInstanceMain.get(Calendar.YEAR);        month = calendarInstanceMain.get(Calendar.MONTH) + 1;        day = calendarInstanceMain.get(Calendar.DAY_OF_MONTH);    }    private View findTimePickerResourceView(DatePicker picker, String name) {        final int id = Resources.getSystem().getIdentifier(name, "id", "android");        return picker.findViewById(id);    }    @Override    boolean hasTitle() {        return false;    }    public static class Builder extends BaseBuilder<Void, DatePickerDialog> {        private DatePickerDialog.OnDateChangeListener listener;        public Builder(Context context) {            super(context);        }        public DatePickerDialog.Builder listener(OnDateChangeListener listener) {            this.listener = listener;            return this;        }        @Override        public DatePickerDialog build() {            DatePickerDialog fragment = new DatePickerDialog();            fragment.setArguments(bundle());            fragment.setListener(listener);            return fragment;        }    }    private void fillMonthsSpinner() {        String[] monthsArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};        monthsArrayAdapter = new ArrayAdapter(getContext(), R.layout.custom_spinner_text_calview_ui2, monthsArray);        monthsArrayAdapter.setDropDownViewResource(R.layout.ui2_custome_dropdown_item_view);        spinnerMonths.setPopupBackgroundDrawable(getContext().getDrawable(R.drawable.popup_menu_background));        spinnerMonths.setAdapter(monthsArrayAdapter);        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            @RequiresApi(api = Build.VERSION_CODES.O)            @Override            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {                String selectedMonth = monthsArrayAdapter.getItem(position);                month = position;                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));                spinnerMonths.setBackground(requireContext().getResources().getDrawable(R.drawable.spinner_cal_view_bg_selected));                ((TextView) adapterView.getChildAt(0)).setTypeface(((TextView) adapterView.getChildAt(0)).getTypeface(), Typeface.BOLD);                updateCalendarOnMonthSelection();            }            @Override            public void onNothingSelected(AdapterView<?> adapter) {            }        });    }    private void updateCalendarOnMonthSelection() {        datePickerViewBinding.datePicker.updateDate(year, month, day);    }    private void updateCalendarOnYearSelection() {        datePickerViewBinding.datePicker.updateDate(year, month, day);    }    private void updateDateTextview() {        String selectedMonthName = new SimpleDateFormat("MMM").format(calendarInstanceMain.getTime());        String selectedDate = day + " " + selectedMonthName + ", " + year;        datePickerViewBinding.tvSelectedDate.setText(selectedDate);    }    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)    private void fillYearSpinner(int startYear) {        //hardcoded year 1970 here for starting the calendar        int arrayLength = currentYear - 1970;        final ArrayList<Integer> yearList = new ArrayList<>();        for (int i = 0; i <= arrayLength; i++) {            yearList.add(startYear);            startYear = startYear - 1;        }        yearArrayAdapter = new ArrayAdapter(getContext(), R.layout.custom_spinner_text_calview_ui2, yearList);        yearArrayAdapter.setDropDownViewResource(R.layout.ui2_custome_dropdown_item_view);        spinnerYear.setPopupBackgroundDrawable(getContext().getDrawable(R.drawable.popup_menu_background));        spinnerYear.setAdapter(yearArrayAdapter);        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            @RequiresApi(api = Build.VERSION_CODES.O)            @Override            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {                year = yearArrayAdapter.getItem(position);                spinnerYear.setBackground(getResources().getDrawable(R.drawable.spinner_cal_view_bg_selected));                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));                ((TextView) adapterView.getChildAt(0)).setTypeface(((TextView) adapterView.getChildAt(0)).getTypeface(), Typeface.BOLD);                updateCalendarOnYearSelection();            }            @Override            public void onNothingSelected(AdapterView<?> adapter) {            }        });    }    public void setMaxDateForDatePicker(int limit) {        int maxYear = currentYear - limit;        calendarInstanceMain.set(maxYear, 11, 31);        datePickerViewBinding.datePicker.setMaxDate(calendarInstanceMain.getTimeInMillis());        updateSpinnerAndTextview(maxYear, 11, 31);        fillYearSpinner(maxYear);    }    private void updateSpinnerAndTextview(int maxYear, int currentMonth, int day) {        //update month spinner        spinnerMonths.setSelection(monthsArrayAdapter.getPosition(String.valueOf(currentMonth)));        //update year spinner        spinnerYear.setSelection(yearArrayAdapter.getPosition(maxYear));    }    public void setMinDateForDatePicker(int limit) {        int minYear = currentYear - limit;        calendarInstanceMain.set(minYear, 0, 1);        datePickerViewBinding.datePicker.setMinDate(calendarInstanceMain.getTimeInMillis());        updateSpinnerAndTextview(minYear, 0, 1);        fillYearSpinner(minYear);    }    private void hideLayoutElements(DialogDatePickerViewBinding datePickerViewBinding) {        final View header = findTimePickerResourceView(Objects.requireNonNull(datePickerViewBinding.datePicker), "date_picker_header");        if (header != null) {            header.setVisibility(View.GONE);        }        final View keyboardIcon = findTimePickerResourceView(datePickerViewBinding.datePicker, "date_picker_year_picker");        if (keyboardIcon != null) {            keyboardIcon.setVisibility(View.GONE);        }        final View nextIcon = findTimePickerResourceView(datePickerViewBinding.datePicker, "next");        if (nextIcon != null) {            nextIcon.setVisibility(View.GONE);        }        final View prevIcon = findTimePickerResourceView(datePickerViewBinding.datePicker, "prev");        if (prevIcon != null) {            prevIcon.setVisibility(View.GONE);        }    }}