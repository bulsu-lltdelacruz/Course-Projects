package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import com.example.aguhan.R;

import database.DatabaseManager;
import database.ModelHistory;
import main.HistoryViewActivity;
import main.util;
import session.SESSION;
import session.TransactionType;
import session.ViewType;

public class HistoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextView tvheader;
    Context c;
    TableLayout table;
    View root;
    TableRow head;
    TableRow emptyPlaceHolder;

    Spinner spnActivityType;
    Spinner spnTransactionType;

    ArrayAdapter<String> activityTypeAdapter;
    ArrayAdapter<String> transactionTypeAdapter;

    private String selectedActivityType = "All";
    private String selectedTransactionType = "All";

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initialize() {
        tvheader = root.findViewById(R.id.title);
        table = root.findViewById(R.id.table);
        head = root.findViewById(R.id.table_head);
        emptyPlaceHolder = root.findViewById(R.id.emptyPlaceholder);
        spnActivityType = root.findViewById(R.id.spnActivityType);
        spnTransactionType = root.findViewById(R.id.spnTransactionType);

        tvheader.setText("Transaction History");
        c = root.getContext();

        setupSpinners();
    }

    private void setupSpinners() {

        String[] activityTypes = {"All", "Account", "Schedule", "Announcement"};
        activityTypeAdapter = new ArrayAdapter<>(
                c,
                android.R.layout.simple_spinner_dropdown_item,
                activityTypes
        );
        spnActivityType.setAdapter(activityTypeAdapter);


        String[] transactionTypes = {"All", "Create", "Edit", "Delete"};
        transactionTypeAdapter = new ArrayAdapter<>(
                c,
                android.R.layout.simple_spinner_dropdown_item,
                transactionTypes
        );
        spnTransactionType.setAdapter(transactionTypeAdapter);
    }

    private void addListeners() {
        spnActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedActivityType = parent.getItemAtPosition(position).toString();
                refreshTable();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spnTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTransactionType = parent.getItemAtPosition(position).toString();
                refreshTable();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void renderTable() {
        View lastDivider = null;
        boolean hasRecords = false;

        for (ModelHistory history : DatabaseManager.historyList) {
            if (history.getAdmin() == null)
                continue;
            if (SESSION.currentAdminAccount != history.getAdmin())
                continue;

            if (!matchesFilters(history))
                continue;

            hasRecords = true;
            emptyPlaceHolder.setVisibility(View.GONE);

            String fullName = history.getAdminFirstName() + " " +
                    history.getAdminMiddleName().substring(0, 1) + ". " +
                    history.getAdminLastName();

            TableRow row = util.createHistoryRow(c, fullName,
                    new String[]{fullName, history.getTimeStamp(), history.getViewType().toString()},
                    history.getId(), history.getViewType(), history.getTransactionType());

            row.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), HistoryViewActivity.class);
                intent.putExtra("id", history.getId());
                startActivity(intent);
            });

            table.addView(row);
            lastDivider = util.createDivider(c);
            table.addView(lastDivider);
        }

        if (lastDivider != null) {
            table.removeView(lastDivider);
        }

        if (!hasRecords) {
            emptyPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private boolean matchesFilters(ModelHistory history) {
        if (!selectedActivityType.equals("All")) {
            ViewType viewType = history.getViewType();
            String viewTypeStr = viewType.toString();

            if (selectedActivityType.equalsIgnoreCase("Account") && viewType != ViewType.ACCOUNT) {
                return false;
            }
            if (selectedActivityType.equalsIgnoreCase("Schedule") && viewType != ViewType.SCHEDULE) {
                return false;
            }
            if (selectedActivityType.equalsIgnoreCase("Announcement") && viewType != ViewType.ANNOUNCEMENT) {
                return false;
            }
        }

        if (!selectedTransactionType.equals("All")) {
            TransactionType transactionType = history.getTransactionType();

            if (selectedTransactionType.equalsIgnoreCase("Create") && transactionType != TransactionType.CREATE) {
                return false;
            }
            if (selectedTransactionType.equalsIgnoreCase("Edit") && transactionType != TransactionType.EDIT) {
                return false;
            }
            if (selectedTransactionType.equalsIgnoreCase("Delete") && transactionType != TransactionType.DELETE) {
                return false;
            }
        }

        return true;
    }

    private void refreshTable() {
        table.removeAllViews();
        table.addView(head);
        table.addView(emptyPlaceHolder);

        renderTable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_history, container, false);
        EdgeToEdge.enable(getActivity());
        initialize();
        addListeners();
        renderTable();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshTable();
    }
}