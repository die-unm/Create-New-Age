package org.antarcticgardens.newage.tools;

import com.mojang.math.Matrix4f;

public class ConversionTool {
    public static Matrix4f toMojang(org.joml.Matrix4f mat) {
        float[] values = new float[16];
        values = mat.get(values);

        Matrix4f mat1 = new Matrix4f();
        ((IMatrix4fValueOperator) (Object) mat1).create_new_age$setValues(values);
        mat1.transpose();

        return mat1;
    }

    public static org.joml.Matrix4f toJoml(Matrix4f mat) {
        float[] values = ((IMatrix4fValueOperator) (Object) mat).create_new_age$getValues();

        org.joml.Matrix4f dest = new org.joml.Matrix4f();
        dest.set(values);
        dest.transpose();

        return dest;
    }
}
