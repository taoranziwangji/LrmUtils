package com.vdolrm.lrmutils.OtherUtils;


import com.vdolrm.lrmutils.LogUtils.MyLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**反射工具类
 * Created by Administrator on 2016/1/26.
 */
public class FieldUtil<T> {

    /**根据类名和私有变量名获取这个私有变量实例，适用于目标反射类为无参构造方法的情况
     *
     * @param className 反射目标类的名字（包名+类名）
     * @param objectName 被反射的私有变量对象的名字
     * @return
     */
    public T getmRecyleAdapter(String className,String objectName){
        try {
            Class<?> demo = null;
            Object obj = null;

            demo = Class.forName(className);
            obj = demo.newInstance();//目标类假如是有无参的构造方法则可以调用这个方法，假如只有有参的会报错
            Field field = demo.getDeclaredField(objectName);
            field.setAccessible(true);
            MyLog.d("反射的" + objectName + "=" + field.get(obj));

            return (T) field.get(obj);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(NoSuchFieldException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }

        return null;
    }


    /**根据类名和私有变量名以及构造方法参数的集合获取这个私有变量实例，适用于目标反射类为有参构造方法的情况
     *
     * @param className 反射目标类的名字（包名+类名）
     * @param objectName 被反射的私有变量对象的名字
     * @param classArray 反射目标类的有参构造方法的参数集合，如：new Class[]{Context.class，…}
     * @param object 反射目标类的有参构造方法的参数对象集合，如：new Object[]{context，…}
     * @return
     */
    public T getmRecyleAdapter(String className,String objectName,Class[] classArray,Object[] object){
        try {
            Class<?> demo = null;
            Object obj = null;

            demo = Class.forName(className);
            Constructor c1=demo.getDeclaredConstructor(classArray);
            c1.setAccessible(true);
            obj=c1.newInstance(object);//对于多个的有参构造方法，只需随便调用一个就行

            Field field = demo.getDeclaredField(objectName);
            field.setAccessible(true);
            MyLog.d("反射的" + objectName + "=" + field.get(obj));

            return (T) field.get(obj);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(NoSuchFieldException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
