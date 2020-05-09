package live.combatemic.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

import androidx.fragment.app.Fragment;

import live.combatemic.app.R;

import live.combatemic.app.model.QuestionAnswer;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class QuestionAnswerFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int lastExpandedPosition = -1;
    private OnListFragmentInteractionListener mListener;
    QuestionAnswerExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionAnswerFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuestionAnswerFragment newInstance(int columnCount) {
        QuestionAnswerFragment fragment = new QuestionAnswerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_answer_list, container, false);

        expListView = (ExpandableListView) view.findViewById(R.id.question_answer_expan);

        listAdapter = new QuestionAnswerExpandableListAdapter(getContext(), QuestionAnswer.ITEMS, mListener);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        ViewTreeObserver vto = expListView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Drawable drawable_groupIndicator =
                        getResources().getDrawable(R.drawable.expandable);
                int drawable_width = drawable_groupIndicator.getMinimumWidth();

                if (android.os.Build.VERSION.SDK_INT <
                        android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    expListView.setIndicatorBounds(
                            expListView.getWidth() - drawable_width,
                            expListView.getWidth());
                } else {
                    expListView.setIndicatorBoundsRelative(
                            expListView.getWidth() - drawable_width,
                            expListView.getWidth());
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(QuestionAnswer.QuestionAnswerModel item);
    }

}
