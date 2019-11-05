package com.clarity

import com.clarity.MapService
import com.clarity.Places
import com.clarity.WeatherService
import org.w3c.dom.Document

import javax.faces.context.FacesContext
import javax.faces.application.Application
import javax.faces.bean.ManagedBean
import javax.faces.bean.RequestScoped
import javax.faces.event.ValueChangeEvent
import javax.faces.model.SelectItem;
import javax.el.ELResolver
import java.util.ArrayList

@ManagedBean()
@RequestScoped

public class Place { 
  String streetAddress = "29419 112th Ave SE",
         city = "Auburn", 
  		 state = "WA",
  		 zip = "98001",
  		 weather = null 
  
  String[] mapUrls = null
  int zoomIndex = 0
    String lat ="1",
    longg = "2"
  
  // CONSTRUCTORS
  
  public Place() {}
  
  public Place(String streetAddress, String city, String state,
		  String[] mapUrls, String weather) {
    setStreetAddress(streetAddress)
    setCity(city)
    setState(state)
    setMapUrls(mapUrls)
    setWeather(weather)
  }
  
  // EVENT HANDLER
  
  public String fetch() {
	FacesContext fc = FacesContext.getCurrentInstance()	    
	ELResolver elResolver = fc.getApplication().getELResolver();


      WeatherService ws = elResolver.getValue(
	              fc.getELContext(), null, "weatherService1");

      Document weather_respond = ws.getWeatherForZip(zip, true, city)
      weather = ws.getWeatherFromDocument(weather_respond)
      lat = ws.getLatFromDocument(weather_respond)
      longg = ws.getLongFromDocument(weather_respond)

      MapService ms = elResolver.getValue(
              fc.getELContext(), null, "mapService1")

      mapUrls = ms.getMap(lat, longg, zoomIndex)

      Places places = elResolver.getValue(
              fc.getELContext(), null, "places")
        places.addPlace(streetAddress, city, state, mapUrls, weather)

    null
  }
  
  public void zoomChanged(ValueChangeEvent e) {
    String value = e.getComponent().getValue();
    zoomIndex = (new Integer(value)).intValue()
  }
  
  // PROPERTY SETTER AND GETTERS
  
  public void setMapUrls(String[] mapUrls) {
	this.mapUrls = mapUrls;
  }
  public String[] getMapUrls() {
    mapUrls == null ? {""} : mapUrls
  }
  
  public String getMapUrl() {
	return mapUrls == null ? "" : mapUrls[zoomIndex]
  }
  
  public String getZoomLevel() {
    return (new Integer(zoomIndex)).toString()
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress
  }
  public String getStreetAddress() { return streetAddress; }
  
  public void setCity(String city) { this.city = city }
  public String getCity() { return city; }
  
  public void setZip(String zip) { this.zip = zip }
  public String getZip() { return zip; }

  public void setWeather(String weather) { this.weather = weather }
  public String getWeather() { return weather; }

  public void setState(String state) { this.state = state }
  public String getState() { return state; }
}
