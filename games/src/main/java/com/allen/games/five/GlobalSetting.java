package com.allen.games.five;

public class GlobalSetting {

    static GlobalSetting instance;
    static int MODE_SINGLE = 1;
    static int MODE_DOUBLE = 2;
    static int MODE_INTERNET= 3;
    
    static int RULE_NONE = 0;
    static int RULE_FORBIDEN = 1;
    
    private int mode ;
    private int rule;
    Style style;
    float mSoundVolume = 0.1f;
    
    String backImagePath;
    String whiteImagePath;
    String blackImagePath;
    
    enum Style {
        STYLE_IN_SCREEN, 
        STYLE_FULL_SCREEN, 
        STYLE_BIG_SCREEN;
    }
    
    GlobalSetting(){
        mode = MODE_SINGLE;
    }
    static GlobalSetting getInstance(){
        if(instance == null){
            instance = new  GlobalSetting();
        }
        return instance;
        
    }
    
    int getMode(){
        return mode;
    }
    
    int getRule(){
        return rule;
        
    }
}
