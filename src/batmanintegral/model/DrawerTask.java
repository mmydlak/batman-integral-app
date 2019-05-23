package batmanintegral.model;

import javafx.concurrent.Task;

import java.util.Random;

public class DrawerTask extends Task {

    private FPoint point;
    private int pointsAmount;
    private final double MIN;
    private final double MAX;
    private NewPointListener listener;
    private Random random;
    private NewPointEvent event;

    public DrawerTask(int pointsAmount, double MIN, double MAX) {
        this.pointsAmount = pointsAmount;
        this.MIN = MIN;
        this.MAX = MAX;
        random = new Random();
        point = new FPoint(0,0);
        event = new NewPointEvent(point,true);

    }

    @Override
    protected Object call() throws Exception {
        for(int i=0; i<pointsAmount; i++){
            generatePoint();
            updateProgress(i, pointsAmount-1);
            if(isCancelled()) {
                break;
            }
        }
        return null;
    }

    private synchronized void generatePoint() {
        point.x = (MIN + (MAX - MIN) * random.nextDouble());
        point.y = (MIN + (MAX - MIN) * random.nextDouble());
        if(Equation.calc(point.x, point.y)) {
            event.belongsToFunction = true;
            listener.newPointDetected(event);
        } else {
            event.belongsToFunction = false;
            listener.newPointDetected(event);
        }
    }

    public void addNewPointListener(NewPointListener listener) {
        this.listener = listener;
    }


}
