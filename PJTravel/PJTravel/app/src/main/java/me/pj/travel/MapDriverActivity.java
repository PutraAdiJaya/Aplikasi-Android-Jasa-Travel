package me.pj.travel;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.pj.travel.controller.ApiClient;
import me.pj.travel.controller.Lokasi;
import me.pj.travel.controller.Preferences;
import me.pj.travel.interfece.ApiInterface;
import me.pj.travel.models.GsonAction;
import me.pj.travel.models.Pesanan;
import me.pj.travel.models.PesananRespon;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapDriverActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "PJ.TRAVEL" ;
    private WebView webView,webViewMap;

    Location mostRecentLocation;
    LocationManager locationManager;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastBestLocation();
        }
    }
    /*AMBIL LOKASI GPS*/
    private Location getLastBestLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return null;
        }
        locationManager.requestLocationUpdates(provider, 1, 10, this);
        mostRecentLocation = locationManager.getLastKnownLocation(provider);

        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

    String mlatitude, mlongitude, malamat;
    @Override
    public void onLocationChanged(Location loc) {

       /* Toast.makeText(
                getBaseContext(),
                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();*/
        String longitude = ""+ loc.getLongitude();
        Log.v(TAG, longitude);
        String latitude =""+ loc.getLatitude();
        Log.v(TAG, latitude);

        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\n "
                + cityName;
        String address = "";
        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> alamat  = geocoder.getFromLocation(Double.valueOf(latitude),Double.valueOf(longitude), 1);
            address = alamat.get(0).getAddressLine(0);
            String city = alamat.get(0).getLocality();
            address += ", " +city;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        malamat = address;
        mlatitude = latitude;
        mlongitude = longitude;
        showInMap(latitude, longitude,address);
        loadData();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
        // TODO Auto-generated method stub

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                &&  checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
             getLastBestLocation();
        }

        mRecyclerView =  findViewById(R.id.recyclerView);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);



    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public static String ucfirst(String subject)
    {
        return Character.toUpperCase(subject.charAt(0)) + subject.substring(1);
    }

    public class JadwaLAdapter extends RecyclerView.Adapter<JadwaLAdapter.MyViewHolder>{
        List<Pesanan> mKontakList;

        public JadwaLAdapter(List <Pesanan> KontakList) {
            mKontakList = KontakList;
        }

        @Override
        public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_news, parent, false);
            MyViewHolder mViewHolder = new MyViewHolder(mView);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder (MyViewHolder holder,final int position){
            holder.mTextViewId.setText(  mKontakList.get(position).getTanggal() + " | " + mKontakList.get(position).getTujuan() + " | " +mKontakList.get(position).getJam());
            holder.mTextViewNama.setText( mKontakList.get(position).getHp());
            holder.mTextViewNomor.setText(  ucfirst(mKontakList.get(position).getNama()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent mIntent = new Intent(view.getContext(), AddActivity.class);
                    mIntent.putExtra("Tanggal", mKontakList.get(position).getId_news());
                    mIntent.putExtra("Pelapor", mKontakList.get(position).getPelapor());
                    mIntent.putExtra("Judul", mKontakList.get(position).getJudul());
                    view.getContext().startActivity(mIntent);
                    */
                    showDataDetail(mKontakList.get(position));
                }
            });
        }

        @Override
        public int getItemCount () {
            return mKontakList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextViewId, mTextViewNama, mTextViewNomor;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTextViewId = (TextView) itemView.findViewById(R.id.tvId);
                mTextViewNama = (TextView) itemView.findViewById(R.id.tvNama);
                mTextViewNomor = (TextView) itemView.findViewById(R.id.tvNomor);
            }
        }
    }

    /*TAMPIL DETALL*/
    Pesanan CURRENT_DATA;
    public void showDataDetail(final Pesanan news){

        CURRENT_DATA = news;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(news.getTujuan() );
        alert.setTitle( "Tujuan : "+ucfirst(   news.getTujuan()) );
        String webData =  "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                "content=\"text/html; charset=utf-8\"> <html><head></head><body id=\"body\"   >"+
                "<h4><b>Nama : </b>"+news.getNama() +"</h4>" +
                "<h4><b>KTP : </b>"+news.getKTP() +"</h4>" +
                "<h4><b>Alamat : </b>"+news.getAlamatPenjemputan() +"</h4>" +
                "<h4><b>Tujuan : </b>"+news.getAlamatTujuan() +"</h4>" +
                "<h4><b>No.HP : </b>"+news.getHp() +"</h4>" +
                "<h4><b>Tanggal : </b>"+news.getTanggal() +"</h4>" +
                "<h4><b>Jam : </b>"+news.getJam() +"</h4>" +
                "<h4><b>No.Pol : </b>"+news.getNoPolisi() +"</h4>" +
                "<h4><b>Armada : </b>"+news.getJenisArmada() +"</h4>" +
                "<h4><b>No.Pol : </b>"+news.getNoPolisi() +"</h4>" +
                "<h4><b>Tarif : </b>"+news.getTarif() +"</h4>" +
                "<h4><b>Status : </b>"+news.getmStatus() +"</h4>" +
                "<h4><b>Pembayaran : </b>"+news.getmPembayaran() +"</h4>" +
                "</body></html>";

        WebView wv = new WebView(this);
        wv.loadData(webData, "text/html", "UTF-8");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("Status Penjemputan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                showStatusJemput();
            }
        });
        alert.setPositiveButton("Lokasi Map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                byte[] array = new byte[7];
                new Random().nextBytes(array);
                String generatedString = new String(array, Charset.forName("UTF-8"));
                webView.loadUrl(ApiClient.MAPS_URL + "?ID="+Preferences.ID+"&DETAIL=0&lat="+String.valueOf(mlatitude)+"&lng="+String.valueOf(mlongitude)+"&Driver="+(Preferences.NAMA)+"&addr="+(malamat)+"&rnd="+generatedString+"&center_lat="+ (news.getLat())+"&center_lng="+ (news.getLng()));

            }
        });
        alert.show();
    }

/*TAMPIL STATUS*/
    private void showStatusJemput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Status Penjemputan");
        builder.setNeutralButton("CLOSE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                });
        builder.setNegativeButton("SUDAH DIJEMPUT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CURRENT_DATA.setmStatus("DIJEMPUT");
                        new SaveDataTask().execute();
                        dialog.cancel();

                    }
                });
        builder.setPositiveButton("BELUM DIJEMPUT",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CURRENT_DATA.setmStatus("BARU");
                        new SaveDataTask().execute();
                        dialog.cancel();

                    }
                });

        builder.show();

    }
    /*REQUEST DATA*/
    private void kirimStatus() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        RequestBody idPemesanan =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getIdPemesanan());

        RequestBody Status =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, CURRENT_DATA.getmStatus());

        Preferences.resetAction(MapDriverActivity.this);
        Call<GsonAction> apiCall = mApiInterface.statusPemesanan(idPemesanan,Status);

        apiCall.enqueue(new Callback<GsonAction>() {
            @Override
            public void onResponse(Call<GsonAction> call, Response<GsonAction>
                    response) {
                try {
                    GsonAction resJSON;
                    resJSON = response.body();
                    Toast.makeText(MapDriverActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    Preferences.IS_ACTION_OK = response.body().getStatus().equals("OK");
                    Preferences.saveAction(MapDriverActivity.this);
                    Log.i("MyAPP", response.body().toString());
                }catch (Exception ex){
                    Toast.makeText(MapDriverActivity.this, "Proses Penghapusan Gagal, Coba lagi", Toast.LENGTH_LONG).show();
                } finally {

                }



            }

            @Override
            public void onFailure(Call<GsonAction> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                Toast.makeText(MapDriverActivity.this, "Proses Penyimpanan Gagal, Silahkan coba lagi", Toast.LENGTH_LONG).show();
            }
        });
    }

/*KIRIM DATA KE SERVER*/

    public class SaveDataTask extends AsyncTask<Void, Void, Boolean> {

        /** progress dialog to show user that the backup is processing. */
        private ProgressDialog dialog;

        public SaveDataTask() {

            Preferences.resetAction(MapDriverActivity.this);
            dialog = new ProgressDialog(MapDriverActivity.this);
            this.dialog.setMessage("Progress start");
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            kirimStatus();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return Preferences.IS_ACTION_OK;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            loadData();
            webView.reload();
        }

        @Override
        protected void onCancelled() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }


    List<Pesanan> DataListPesanan;
    ApiInterface mApiInterface;
    List<Lokasi>  Lokasi_USER ;

    /*TAMPIL DATA*/
    private void loadData() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        RequestBody Driver =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, Preferences.NAMA);

        Preferences.resetAction(MapDriverActivity.this);
        Call<PesananRespon> apiCall = mApiInterface.getPesananDriver(Driver);
        apiCall.enqueue(new Callback<PesananRespon>() {
            @Override
            public void onResponse(Call<PesananRespon> call, Response<PesananRespon>
                    response) {
                if (response.body().getStatus().equals( "OK"))
                {
                    List<Pesanan> DataListPesanan__ = response.body().getPesanan();


                    List<Pesanan> DataListPesanan_ = new ArrayList<>();
                    for (Pesanan Ps : DataListPesanan__) {
                        if (Ps.getmStatus().equals("DIJEMPUT")) continue;
                        DataListPesanan_.add(Ps);
                    }

                    Log.d("Retrofit Get", "Jumlah data : " +
                            String.valueOf(DataListPesanan_.size()));
                    Lokasi_USER = new ArrayList<>();

                    if (DataListPesanan_ != null) {
                        Lokasi Lk = new Lokasi("0","DRIVER",Double.parseDouble(mlatitude),Double.parseDouble(mlongitude));
                        Lokasi_USER.add(Lk);
                        for (Pesanan Ps : DataListPesanan_){

                            Lk = new Lokasi(Ps.getIdPemesanan(),Ps.getNama(),Double.parseDouble(Ps.getLat()),Double.parseDouble(Ps.getLng()
                                ));
                            Lokasi_USER.add(Lk);
                        }
                        hitungDjixstra(DataListPesanan_);


                    }else {
                        Toast.makeText(MapDriverActivity.this,"Data Pelanggan Tidak Ditemukan, di Hari ini...", Toast.LENGTH_LONG).show();
                    }

                }

                Log.i("MyAPP",response.message() );
            }

            @Override
            public void onFailure(Call<PesananRespon> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
/*PROSES ALGORITMA*/
    private void hitungDjixstra(List<Pesanan> dataListPesanan_) {
        int adjMatrix[][] = new int[Lokasi_USER.size()][Lokasi_USER.size()];
        /* MENDAPATKAN MATRIK JARAK*/
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                LatLng locA = Lokasi_USER.get(i).latLng;
                LatLng locB = Lokasi_USER.get(j).latLng;
                adjMatrix[i][j] = (int) meterDistanceBetweenPoints(locA.latitude,locA.longitude,locB.latitude,locB.longitude);
            }
        }

        /*CEK SEMUA NODE TIDAK DIKUNJUNGI*/
        boolean[] isVisited = new boolean[Lokasi_USER.size()];
        for (int i = 0; i < isVisited.length; i++) {
            isVisited[i] = false;
        }
        boolean isSolve = false;
        int starIndex = 0;
        isVisited[starIndex] = true;
        List<Integer> Path = new ArrayList<>();
        /*PROSES PERULANGAN SAMPAI SOLUSI TERCAPAI*/
        while (!isSolve){
            /* SET JARAK TERPEDEK DENGAN NILAI MAKSIMAL */
            int minDistance = Integer.MAX_VALUE;
            int visitedNode = starIndex;
            /* MENCARI JARAK TERPEDEK DARI MATRIK JARAK */
            for (int i = 0; i < adjMatrix.length; i++) {
                if (!isVisited[i] ){
                    /*JIKA JARAK DITEMUKAN MAKA UPDATE STATUS NODE MENDJADI VISITED*/
                    if (adjMatrix[starIndex][i]<minDistance){
                        minDistance = adjMatrix[starIndex][i];
                        visitedNode = i;
                    }
                }
            }
            /*MEMAMBAHKAN RUTE KE TABEL SOLUSI*/
            if (visitedNode!=starIndex){
                isVisited[visitedNode] = true;
                Path.add(visitedNode-1);
            }
            /*CEK APAKAH ADA NODE YANG BELUM DIKUNJUNNGI*/
            isSolve = true;
            for (int i = 0; i < isVisited.length; i++) {
                if (!isVisited[i]){
                    isSolve = false;
                }
            }
            /*PROSES BERHAENTI JIKA SEMUA SUDAH DIKUNJUNGI ATAU SOLUSI TERCAPAI*/
        }


        /*UPDATE RUTE TERDEKAT DARI TABLE PESANAN BERDASAKAN ALOGRITMA DJIKSTRA*/
        DataListPesanan = new ArrayList<>();
        for (Integer ix : Path){

            DataListPesanan.add(dataListPesanan_.get(ix));
        }


        mAdapter = new JadwaLAdapter(DataListPesanan);
        mRecyclerView.setAdapter(mAdapter);

    }
/*JARAK DALAM METER*/
    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        Location selected_location=new Location("locationA");
        selected_location.setLatitude(lat_a);
        selected_location.setLongitude(lng_a);
        Location near_locations=new Location("locationB");
        near_locations.setLatitude(lat_b);
        near_locations.setLongitude(lng_b);
        double distance=selected_location.distanceTo(near_locations);
        return distance;
    }
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    /*TAMPIL PETA*/
    private void showInMap(String latitude, String longitude,String alamat ) {


        webView = findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                // TODO Auto-generated method stub
                Log.v("ChromeClient", "invoked: onConsoleMessage() - " + sourceID + ":"
                        + lineNumber + " - " + message);
                super.onConsoleMessage(message, lineNumber, sourceID);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.v("ChromeClient", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId() );

                return true;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin,
                                                           final GeolocationPermissions.Callback callback) {
                final boolean remember = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MapDriverActivity.this);
                builder.setTitle("Acces Permission..");
                builder.setMessage(
                        "Allow your application to access your location? ")
                        .setCancelable(true)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        callback.invoke(origin, true, remember);
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        callback.invoke(origin, false, remember);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String address) {
               view.loadUrl("javascript:console.log('TAG'+document.getElementsByTagName('html')[0].innerHTML);");

            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setDatabasePath("/data/data/" + webView.getContext().getPackageName() + "/databases/");
        }
        webView.getSettings().setGeolocationDatabasePath(
                "/data/data/me.pj.travel");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        webView.loadUrl(ApiClient.MAPS_URL  + "?ID="+Preferences.ID+"&DETAIL=1&lat="+String.valueOf(latitude)+"&lng="+String.valueOf(longitude)+"&Driver="+(Preferences.NAMA)+"&addr="+(alamat)+"&rnd="+generatedString+"&center_lat="+String.valueOf(latitude)+"&center_lng="+String.valueOf(longitude));

    }
    public static String encode(String url)
    {
        try {
            String encodeURL= URLEncoder.encode( url, "UTF-8" );
            return encodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while encoding" +e.getMessage();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
}
