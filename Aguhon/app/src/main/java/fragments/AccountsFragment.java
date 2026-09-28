package fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.aguhan.R;

import database.AccountType;
import database.STRING;
import main.AccountDetailsActivity;

public class AccountsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View root;
    private LinearLayout llStudents;
    private LinearLayout llTeachers;
    private LinearLayout llAdmins;

    public AccountsFragment() {

    }
    public static AccountsFragment newInstance(String param1, String param2) {
        AccountsFragment fragment = new AccountsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_accounts, container, false);
        setupViews();
        addListeners();
        EdgeToEdge.enable(getActivity());

        return root;
    }

    private void addListeners() {
        llStudents.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountDetailsActivity.class);
            intent.putExtra("accountType", AccountType.STUDENT);
            startActivity(intent);
        });
        llTeachers.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountDetailsActivity.class);
            intent.putExtra("accountType", AccountType.TEACHER);
            startActivity(intent);
        });
        llAdmins.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountDetailsActivity.class);
            intent.putExtra("accountType", AccountType.ADMIN);
            startActivity(intent);
        });
    }

    private void setupViews() {
        llStudents = root.findViewById(R.id.llViewStudentAccounts);
        llTeachers = root.findViewById(R.id.llViewTeachertAccounts);
        llAdmins = root.findViewById(R.id.llViewAdminAccounts);
    }
}