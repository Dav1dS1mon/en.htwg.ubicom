package ubicom.htwg.en.healthapp.Controller.BioHarnessBT;

public interface ConnectedListener<T> {
	public void Connected(ConnectedEvent<T> eventArgs);
}
