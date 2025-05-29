package com.newpos.store.android.sdk.dto;

import java.util.List;

/**
 * @ClassName : Ability
 * @Author : zhouqiang(1376359644@qq.com)
 * @Email : newpos@newpostech.com
 * @Date : 2024/4/26-16:53
 * @Version : 1.0
 * @Description :
 * @website : https://www.newpostech.com/
 */
public class Ability {
    private EAbility eAbility;
    private List<String> apis;
    private String name;

    public Ability(EAbility eAbility, List<String> apis) {
        this.eAbility = eAbility;
        this.apis = apis;
    }

    public EAbility geteAbility() {
        return eAbility;
    }

    public void seteAbility(EAbility eAbility) {
        this.eAbility = eAbility;
    }

    public List<String> getApis() {
        return apis;
    }

    public void setApis(List<String> apis) {
        this.apis = apis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "eAbility=" + eAbility +
                ", apis=" + apis +
                ", name='" + name + '\'' +
                '}';
    }
}
