package com.hh.ota.classuntils;

import java.util.List;

public class srcFileObject {
    public String partitionName;               // Embed
    public List<updateObject> objList;
    public String version;
    public int  isEmbed;                         // 1: Embed    0:Ethernet
    public int  isDemarcate;
}
