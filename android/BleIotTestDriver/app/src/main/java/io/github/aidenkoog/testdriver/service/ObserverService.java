package io.github.aidenkoog.testdriver.service;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class ObserverService {

    private static final String TAG = ObserverService.class.getSimpleName();

    HashMap<String, ObservableObject> mObservables;

    class ObservableObject extends Observable {
        public void doChange() {
            setChanged();
        }

        public void clearChange() {
            clearChanged();
        }
    }

    private ObserverService() {
        mObservables = new HashMap<String, ObservableObject>();
    }

    private static class SingletonData {
        private static final ObserverService instance = new ObserverService();
    }

    public static ObserverService getInstance() {
        return SingletonData.instance;
    }

    public void addObserver(String notification, Observer observer) {
        ObservableObject observable = mObservables.get(notification);
        if (observable == null) {
            observable = new ObservableObject();
            mObservables.put(notification, observable);
        }
        observable.addObserver(observer);
    }

    public void removeObserver(String notification, Observer observer) {
        ObservableObject observable = mObservables.get(notification);
        if (observable!=null) {         
            observable.deleteObserver(observer);
        }
    }       

    public void postNotification(String notification, Object object) {
        ObservableObject observable = mObservables.get(notification);
        if (observable != null) {
            observable.doChange();
            observable.notifyObservers(object);
        }
    }
}
