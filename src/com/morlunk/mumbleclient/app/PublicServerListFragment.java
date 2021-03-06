package com.morlunk.mumbleclient.app;

import java.util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.morlunk.mumbleclient.Globals;
import com.morlunk.mumbleclient.R;
import com.morlunk.mumbleclient.Settings;
import com.morlunk.mumbleclient.app.db.DbAdapter;
import com.morlunk.mumbleclient.app.db.PublicServer;

/**
 * Displays a list of public servers that can be connected to, sorted, and favourited.
 * @author morlunk
 *
 */
public class PublicServerListFragment extends SherlockFragment implements OnItemClickListener {
	
	public static final int MAX_ACTIVE_PINGS = 50;
	
	private ServerConnectHandler connectHandler;
	private GridView serverGrid;
	private ProgressBar serverProgress;
	private PublicServerAdapter serverAdapter;
	private int activePingCount = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			connectHandler = (ServerConnectHandler)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" must implement ServerConnectHandler!");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_public_server_list, container, false);
		serverGrid = (GridView) view.findViewById(R.id.serverGrid);
		serverGrid.setOnItemClickListener(this);
        if(serverAdapter != null)
            serverGrid.setAdapter(serverAdapter);
		serverProgress = (ProgressBar) view.findViewById(R.id.serverProgress);
		serverProgress.setVisibility(serverAdapter == null ? View.VISIBLE : View.GONE);
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_public_server_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (isFilled()) {
			switch(item.getItemId()) {
			case R.id.menu_sort_server_item:
				showSortDialog();
				return true;
			case R.id.menu_search_server_item:
				showFilterDialog();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setServers(List<PublicServer> servers) {
		serverProgress.setVisibility(View.GONE);
		serverAdapter = new PublicServerAdapter(getActivity(), servers);
		serverGrid.setAdapter(serverAdapter);
	}
	
	public boolean isFilled() {
		return serverAdapter != null;
	}
	
	private void showSortDialog() {
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
		alertBuilder.setTitle(R.string.sortBy);
		alertBuilder.setItems(new String[] { getString(R.string.name), getString(R.string.country)}, new SortClickListener());
		alertBuilder.show();
	}
	
	private void showFilterDialog() {
		View dialogView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_server_search, null);
		final EditText nameText = (EditText) dialogView.findViewById(R.id.server_search_name);
		final EditText countryText = (EditText) dialogView.findViewById(R.id.server_search_country);
				
		final AlertDialog dlg = new AlertDialog.Builder(getActivity()).
		    setTitle(R.string.search).
		    setView(dialogView).
		    setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
		        public void onClick(final DialogInterface dialog, final int which)
		        {
					String queryName = nameText.getText().toString().toUpperCase(Locale.US);
					String queryCountry = countryText.getText().toString().toUpperCase(Locale.US);
					serverAdapter.filter(queryName, queryCountry);
		            dialog.dismiss();
		        }
		    }).create();
		
		nameText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		nameText.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event)
		    {
				String queryName = nameText.getText().toString().toUpperCase(Locale.US);
				String queryCountry = countryText.getText().toString().toUpperCase(Locale.US);
				serverAdapter.filter(queryName, queryCountry);
		        dlg.dismiss();
		        return true;
		    }
		});
		
		countryText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		countryText.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event)
		    {
				String queryName = nameText.getText().toString().toUpperCase(Locale.US);
				String queryCountry = countryText.getText().toString().toUpperCase(Locale.US);
				serverAdapter.filter(queryName, queryCountry);
		        dlg.dismiss();
		        return true;
		    }
		});
		
		// Show keyboard automatically
		nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus) {
		            dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		        }
		    }
		});
		
		dlg.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		connectHandler.connectToPublicServer(serverAdapter.getItem(arg2));
	}
	
	private class PublicServerAdapter extends ArrayAdapter<PublicServer> {
		private Map<PublicServer, ServerInfoResponse> infoResponses = new HashMap<PublicServer, ServerInfoResponse>();
		private List<PublicServer> originalServers;
		
		public PublicServerAdapter(Context context, List<PublicServer> servers) {
			super(context, android.R.id.text1, servers);
			originalServers = new ArrayList<PublicServer>(servers);
		}
		
		public void filter(String queryName, String queryCountry) {
			clear();

			for(PublicServer server : originalServers) {
                /*
                 * It'll include a server in the results if...
                 * - There is a blank query string specified for the category (name or country)
                 * - The category value is not null and the search string contains it
                 */
                boolean nameFound = "".equals(queryName) || (server.getName() != null && server.getName().toUpperCase(Locale.getDefault()).contains(queryName));
                boolean countryFound = "".equals(queryCountry) || (server.getCountry() != null && server.getCountry().toUpperCase().contains(queryCountry));

				if(nameFound && countryFound)
					add(server);
			}
		}

		@SuppressLint("NewApi")
		@Override
		public final View getView(
			final int position,
			final View v,
			final ViewGroup parent) {
			View view = v;
			
			if(v == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(
					R.layout.public_server_list_row,
					null);
			}
			
			final PublicServer server = getItem(position);
			view.setTag(server);
			
			TextView nameText = (TextView) view.findViewById(R.id.server_row_name);
			TextView addressText = (TextView) view.findViewById(R.id.server_row_address);
			
			if(server.getName().equals("")) {
				nameText.setText(server.getHost());
			} else {
				nameText.setText(server.getName());
			}
			
			addressText.setText(server.getHost()+":"+server.getPort());
			
			TextView locationText = (TextView) view.findViewById(R.id.server_row_location);
			locationText.setText(server.getCountry());
			
			// Ping server if available
			if(!infoResponses.containsKey(server) && activePingCount < MAX_ACTIVE_PINGS) {
				activePingCount++;
				final View serverView = view; // Create final instance of view for use in asynctask
				ServerInfoTask task = new ServerInfoTask() {
					protected void onPostExecute(ServerInfoResponse result) {
						super.onPostExecute(result);
						infoResponses.put(server, result);
						if(serverView != null && serverView.isShown() && serverView.getTag() == server)
							updateInfoResponseView(serverView, server);
						activePingCount--;
						Log.d(Globals.LOG_TAG, "DEBUG: Servers remaining in queue: "+activePingCount);
					};
				};
				
				// Execute on parallel threads if API >= 11. RACE CAR THREADING, WOOOOOOOOOOOOOOOOOOOOOO
				if(VERSION.SDK_INT >= 11)
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, server);
				else
					task.execute(server);
			}
			
			ImageView favoriteButton = (ImageView)view.findViewById(R.id.server_row_favorite);
			
			favoriteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

                    Settings settings = Settings.getInstance(getActivity());

					// Allow username entry
					final EditText usernameField = new EditText(getContext());
					usernameField.setHint(settings.getDefaultUsername());
					alertBuilder.setView(usernameField);

					alertBuilder.setTitle(R.string.addFavorite);
					
					alertBuilder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
                            String username = usernameField.getText().toString();
                            if(username.equals(""))
                                username = usernameField.getHint().toString();

							DbAdapter adapter = new DbAdapter(getActivity());
							adapter.open();
							adapter.createServer(server.getName(), server.getHost(), server.getPort(), username, "");
							adapter.close();
							connectHandler.publicServerFavourited();
						}
					});
					
					alertBuilder.show();
				}
				
			});
			
			updateInfoResponseView(view, server);
			
			return view;
		}
		
		private void updateInfoResponseView(View view, PublicServer server) {
			ServerInfoResponse infoResponse = infoResponses.get(server);
			// If there is a null value for the server info (rather than none at all), the request must have failed.
			boolean requestExists = infoResponse != null;
			boolean requestFailure = infoResponse != null && infoResponse.isDummy();
			
			TextView serverVersionText = (TextView) view.findViewById(R.id.server_row_version_status);
			TextView serverUsersText = (TextView) view.findViewById(R.id.server_row_usercount);
			ProgressBar serverInfoProgressBar = (ProgressBar) view.findViewById(R.id.server_row_ping_progress);
			
			serverVersionText.setVisibility(!requestFailure && !requestExists ? View.INVISIBLE : View.VISIBLE);
			serverUsersText.setVisibility(!requestFailure && !requestExists ? View.INVISIBLE : View.VISIBLE);
			serverInfoProgressBar.setVisibility(!requestExists ? View.VISIBLE : View.INVISIBLE);
			
			if(infoResponse != null && !requestFailure) {
				serverVersionText.setText(getResources().getString(R.string.online)+" ("+infoResponse.getVersionString()+")");
				serverUsersText.setText(infoResponse.getCurrentUsers()+"/"+infoResponse.getMaximumUsers());
			} else if(requestFailure) {
				serverVersionText.setText(R.string.offline);
				serverUsersText.setText("");
			} else {
				serverVersionText.setText(R.string.noServerInfo);
			}
		}
	}
	
	private class SortClickListener implements DialogInterface.OnClickListener {

		private static final int SORT_NAME = 0;
		private static final int SORT_COUNTRY = 1;
		
		private Comparator<PublicServer> nameComparator = new Comparator<PublicServer>() {
			@Override
			public int compare(PublicServer lhs, PublicServer rhs) {
                // Handle null values and put them at the bottom (this is a real possibility!)
                if (rhs.getName() == null)
                    return (lhs.getName() == null) ? 0 : -1;
                if (lhs.getName() == null)
                    return 1;
				return lhs.getName().compareTo(rhs.getName());
			}
		};

		private Comparator<PublicServer> countryComparator = new Comparator<PublicServer>() {
			@Override
			public int compare(PublicServer lhs, PublicServer rhs) {
                // Handle null values and put them at the bottom (this is a real possibility!)
                if (rhs.getCountry() == null)
                    return (lhs.getCountry() == null) ? 0 : -1;
                if (lhs.getCountry() == null)
                    return 1;
				return lhs.getCountry().compareTo(rhs.getCountry());
			}
		};
		
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			ArrayAdapter<PublicServer> arrayAdapter = serverAdapter;
			if(which == SORT_NAME) {
				arrayAdapter.sort(nameComparator);
			} else if(which == SORT_COUNTRY) {
				arrayAdapter.sort(countryComparator);
			}
		}
	}
}
