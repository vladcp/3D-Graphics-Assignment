package ui;

import javax.swing.*;

import core.lights.Light;
import core.lights.Spotlight;
import start.Hatch;
import start.Hatch_GLEventListener;

import java.awt.*;

public class GUI extends JPanel{

  JButton L1_POS1;
  JButton L1_POS2;
  JButton L1_POS3;

  JButton lastClickedButton = new JButton();
  public GUI(Hatch_GLEventListener hatch){
    super();

    this.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
    
    JPanel west = new JPanel();
    west.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
    west.add(new JLabel("Light Switches"));
    this.add(west);

    addLightSliders(west, hatch);
    L1_POS1 = addButton(west, "L1_POS1", hatch);
    L1_POS2 = addButton(west, "L1_POS2", hatch);
    L1_POS3 = addButton(west, "L1_POS3", hatch);
    // addButton(west, "Toggle Spotlight 2");
  }

  /**
   * Add button to panel 
   * @param panel
   * @param btnName
   */
  private JButton addButton(JPanel panel, String btnName, Hatch_GLEventListener hatch){
    JButton button = new JButton(btnName);

    button.addActionListener(e -> {
      lastClickedButton.setEnabled(true);
      button.setEnabled(false);
      lastClickedButton = button;

      String action = button.getActionCommand();
      onButtonClick(action, hatch);
    });

    panel.add(button);
    return button;
  }
  /**
   * Add Light sliders to panel
   * @param panel
   */
  private void addLightSliders(JPanel panel, Hatch_GLEventListener hatch){
    final int MIN = 0, MAX = 100, CURRENT = (int) Light.DEFAULT_INTENS_L1 * 100;
    
    // light 1 sliders
    JSlider light1Slider = new JSlider(MIN, MAX, CURRENT);
    panel.add(new JLabel("Light 1"));
    panel.add(light1Slider);
    light1Slider.addChangeListener(e -> 
      onLightSliderChange(light1Slider.getValue(), hatch.getLight1()));

    // light 2 sliders
    JSlider light2Slider = new JSlider(MIN, MAX, CURRENT);
    panel.add(new JLabel("Light 2"));
    panel.add(light2Slider);
    light2Slider.addChangeListener(e -> 
      onLightSliderChange(light2Slider.getValue(), hatch.getLight2()));
  }

  private void onLightSliderChange(float value, Light light){
    light.setIntensity(value / 100f);
  }

  public void onButtonClick(String buttonName, Hatch_GLEventListener hatch) {
    switch(buttonName) {
      case "L1_POS1":
        //animate lamp 1
        hatch.getLamp1().initialiseAnimation(1);
      break;
      case "L1_POS2":
        //animate lamp 1
        hatch.getLamp1().initialiseAnimation(2);
      break;
      case "L1_POS3":
        //animate lamp 1
        hatch.getLamp1().initialiseAnimation(3);
      break;
      default:
        System.err.println(buttonName + " is not a button.");
    }
  }
}
