var TNService=function(){};function Address(a,b,c,d,e,f,g,h,i,j,k){this.id=a;this.selectedIndex=b;this.type=c;this.phoneNumber=d;this.label=e;this.status=f;this.sharedFromPTN=g;this.sharedFromUser=h;this.stop=j;this.category=i;this.poi=k}
function Stop(a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p){this.type=a;this.lat=b;this.lon=c;this.firstLine=d;this.lastLine=e;this.status=f;this.stopId=g;this.city=h;this.zip=i;this.country=j;this.isGeocoded=k;this.province=l;this.streetName=m;this.streetNumber=n;this.label=o;this.crossStreet=p}function Poi(a,b,c,d,e){this.rated=a;this.rating=b;this.rateNumber=c;this.popularity=d;this.type=e}TNService.prototype.captureAddress=function(a,b,c){PhoneGap.exec("AddressService.getAddress",b,c,[a])};
TNService.prototype.getAddressBook=function(a,b,c){PhoneGap.exec("AddressService.getAddressBook",b,c,[a])};TNService.prototype.setAddress=function(a,b,c,d){PhoneGap.exec("AddressService.setAddressByType",c,d,[a,b])};TNService.prototype.getAddress=function(a,b,c){PhoneGap.exec("AddressService.getAddressByType",b,c,[a])};TNService.prototype.getHomeAddress=function(a,b){PhoneGap.exec("AddressService.getHomeAddress",a,b)};
TNService.prototype.logEvent=function(a,b,c,d){PhoneGap.exec("LogEventService.logEvent",c,d,[a,b])};TNService.prototype.displayMap=function(a,b,c,d){PhoneGap.exec("MapService.doMap",c,d,[a,b])};TNService.prototype.getMapRegionList=function(a,b,c){PhoneGap.exec("MapService.getRegionList",b,c)};TNService.prototype.startMapDownload=function(a,b,c,d){PhoneGap.exec("MapService.startDownload",c,d,[a])};TNService.prototype.stopMapDownload=function(a,b,c,d){PhoneGap.exec("MapService.stopDownload",c,d,[a])};
TNService.prototype.pauseMapDownload=function(a,b,c,d){PhoneGap.exec("MapService.pauseDownload",c,d,[a])};TNService.prototype.deleteMapRegion=function(a,b,c,d){PhoneGap.exec("MapService.deleteRegion",c,d,[a])};TNService.prototype.navTo=function(a,b,c,d,e){PhoneGap.exec("NavService.doNav",d,e,[a,b,c])};TNService.prototype.getPersistentData=function(a,b,c){PhoneGap.exec("PersistentDataService.getPersistentData",b,c,[a])};
TNService.prototype.setPersistentData=function(a,b,c,d,e){PhoneGap.exec("PersistentDataService.setPersistentData",d,e,[a,b,c])};TNService.prototype.deletePersistentData=function(a,b,c){PhoneGap.exec("PersistentDataService.deletePersistentData",b,c,[a])};TNService.prototype.getPreference=function(a,b,c){PhoneGap.exec("PreferenceService.getPreference",b,c,[a])};
TNService.prototype.setPreference=function(a,b,c,d,e){var f={};f.url=c;f.type=a;f.value=b;PhoneGap.exec("PreferenceService.setPreference",d,e,f)};TNService.prototype.syncPurchase=function(a,b,c){var d={};d.type=a;PhoneGap.exec("PreferenceService.syncPurchase",b,c,d)};TNService.prototype.getSyncPurchaseStatus=function(a,b,c){var d={};d.type=a;PhoneGap.exec("PreferenceService.getSyncPurchaseStatus",b,c,d)};
TNService.prototype.completeAppstorePurchase=function(a,b,c){PhoneGap.exec("PreferenceService.completeAppstorePurchase",b,c,[a])};TNService.prototype.syncResource=function(a,b){PhoneGap.exec("PreferenceService.syncResource",a,b,[])};TNService.prototype.getTeleNavToken=function(a,b){PhoneGap.exec("TNTokenService.getTelenavToken",a,b,[])};TNService.prototype.getTNInfo=function(a,b){PhoneGap.exec("TNInfoService.getTNInfo",a,b,[])};
TNService.prototype.getPointOfInterest=function(a,b,c){PhoneGap.exec("TNInfoService.getPointOfInterest",b,c,[a])};TNService.prototype.setCredentials=function(a,b,c){PhoneGap.exec("TNInfoService.setCredentials",b,c,[a])};TNService.prototype.setWindowMode=function(a,b,c){var d={};d.windowMode=a;PhoneGap.exec("TNWindowModeService.setWindowMode",b,c,d)};TNService.prototype.exitBrowser=function(a,b,c){PhoneGap.exec("TNUtilService.exitBrowser",b,c,[a])};
TNService.prototype.launchLocalApp=function(a,b,c){var d={};d.utilAction=a;PhoneGap.exec("TNUtilService.launchLocalApp",b,c,d)};TNService.prototype.launchSettings=function(a,b){PhoneGap.exec("TNUtilService.launchSettings",a,b)};PhoneGap.addConstructor(function(){if(typeof navigator.tnservice=="undefined")navigator.tnservice=new TNService;TNService.prototype.geolocation=navigator._geo});