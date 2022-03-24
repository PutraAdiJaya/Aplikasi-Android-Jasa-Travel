package me.pj.travel.controller;

import com.google.android.gms.maps.model.LatLng;

/*KLAS UNTUK HANDLE LOKASI GPS*/
public class Lokasi {
	public String ID;
	public String Name;
	public double Lat;
	public double Lng;
	
	public LatLng latLng;
	
	public Lokasi(String ID_,
		String Name_,
		double Lat_,
		double Lng_){
		this.ID = ID_;
		this.Name = Name_;
		this.Lat = Lat_;
		this.Lng = Lng_;
		
		latLng = new LatLng(Lat, Lng); 
		
	} 
}
