package ui;

import javax.swing.*;

import core.lights.Light;
import start.Hatch_GLEventListener;

import java.awt.*;
/**
 * GUI class, responsible for the appearance and behaviour
 * of the sliders and buttons 
 * @author Vlad Prisacariu
 */
public class GUI extends JPanel{

  JButton L1_POS1;
  JButton L1_POS2;
  JButton L1_POS3;
  JButton SP1;
  JButton SP2;

  JButton lastClickedButton = new JButton();
  public GUI(Hatch_GLEventListener hatch){
    super();

    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    
    JPanel sliders = new JPanel();
    // sliders.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
    sliders.add(new JLabel("Light Sliders"));
    this.add(sliders);

    JPanel posButtons = new JPanel();
    posButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    posButtons.add(new JLabel("Lamp Postures"));
    // east.setBounds(10,10,300,200);
    this.add(posButtons);

    JPanel spotlights = new JPanel();
    spotlights.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    spotlights.add(new JLabel("Spotlights"));
    // east.setBounds(10,10,300,200);
    this.add(spotlights);

    addLightSliders(sliders, hatch);
    L1_POS1 = addButton(posButtons, "L1_POS1", hatch, true);
    L1_POS2 = addButton(posButtons, "L1_POS2", hatch, true);
    L1_POS3 = addButton(posButtons, "L1_POS3", hatch, true);
    L1_POS3 = addButton(posButtons, "L2_POS1", hatch, true);
    L1_POS3 = addButton(posButtons, "L2_POS2", hatch, true);
    L1_POS3 = addButton(posButtons, "L2_POS3", hatch, true);

    SP1 = addButton(spotlights, "Toggle Spotlight 1", hatch, false);
    SP2 = addButton(spotlights, "Toggle Spotlight 2", hatch, false);
  }

  /**
   * Add button to panel 
   * @param panel
   * @param btnName
   */
  private JButton addButton(JPanel panel, String btnName, Hatch_GLEventListener hatch, boolean disableOnClick){
    JButton button = new JButton(btnName);

    button.addActionListener(e -> {
      if (disableOnClick) {
        lastClickedButton.setEnabled(true);
        button.setEnabled(false);
        lastClickedButton = button;
      }

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
    final int MIN = 0, MAX = 100, CURRENT = (int) (Light.DEFAULT_INTENS_L1 * 100);
    
    // light 1 sliders
    JSlider light1Slider = new JSlider(MIN, MAX, CURRENT);
    panel.add(new JLabel("Light 1"));
    panel.add(light1Slider);
    light1Slider.addChangeListener(e -> {
      onLightSliderChange(light1Slider.getValue(), hatch.getLight1());
    });

    // light 2 sliders
    JSlider light2Slider = new JSlider(MIN, MAX, CURRENT);
    panel.add(new JLabel("Light 2"));
    panel.add(light2Slider);
    light2Slider.addChangeListener(e -> {
      onLightSliderChange(light2Slider.getValue(), hatch.getLight2());
    });
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
      case "L2_POS1":
        //animate lamp 1
        hatch.getLamp2().initialiseAnimation(4);
      break;
      case "L2_POS2":
        //animate lamp 1
        hatch.getLamp2().initialiseAnimation(5);
      break;
      case "L2_POS3":
        //animate lamp 1
        hatch.getLamp2().initialiseAnimation(6);
      break;
      case "Toggle Spotlight 1":
        //animate lamp 1
        hatch.getSpotlight1().toggle();
      break;
      case "Toggle Spotlight 2":
        //animate lamp 1
        hatch.getSpotlight2().toggle();
      break;
      default:
        System.err.println(buttonName + " is not a button.");
    }
  }
}
