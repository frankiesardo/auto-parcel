package android.auto.value.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Person person = Person.create("Frankie", 123);
    Toast.makeText(this, person.name(), Toast.LENGTH_SHORT).show();
  }
}
