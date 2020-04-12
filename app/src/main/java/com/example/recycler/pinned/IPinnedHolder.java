package com.example.recycler.pinned;

/**
 * Created by Rex.Wei on 2020-04-12
 *
 * @author 韦玉庭
 */
public interface IPinnedHolder {
    int TYPE_PARENT = 100;
    int TYPE_CHILD = 200;

    boolean isPinned();

}
