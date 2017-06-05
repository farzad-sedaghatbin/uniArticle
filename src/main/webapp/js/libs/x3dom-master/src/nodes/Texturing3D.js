/*
 * X3DOM JavaScript Library
 * http://www.x3dom.org
 *
 * (C)2009 Fraunhofer IGD, Darmstadt, Germany
 * Dual licensed under the MIT and GPL
 *
 * Based on code originally provided by
 * Philip Taylor: http://philip.html5.org
 */

/* ### X3DTexture3DNode ### */
x3dom.registerNodeType(
    "X3DTexture3DNode",
    "Texturing3D",
    defineClass(x3dom.nodeTypes.X3DTextureNode,
        function (ctx) {
            x3dom.nodeTypes.X3DTexture3DNode.superClass.call(this, ctx);
        }
    )
);

/* ### ComposedTexture3D ### */
x3dom.registerNodeType(
    "ComposedTexture3D",
    "Texturing3D",
    defineClass(x3dom.nodeTypes.X3DTexture3DNode,
        function (ctx) {
            x3dom.nodeTypes.ComposedTexture3D.superClass.call(this, ctx);

            this.addField_MFNode('texture', x3dom.nodeTypes.X3DTexture3DNode);
        }
    )
);

/* ### ImageTexture3D ### */
x3dom.registerNodeType(
    "ImageTexture3D",
    "Texturing3D",
    defineClass(x3dom.nodeTypes.X3DTexture3DNode,
        function (ctx) {
            x3dom.nodeTypes.ImageTexture3D.superClass.call(this, ctx);
        }
    )
);

/* ### PixelTexture3D ### */
x3dom.registerNodeType(
    "PixelTexture3D",
    "Texturing3D",
    defineClass(x3dom.nodeTypes.X3DTexture3DNode,
        function (ctx) {
            x3dom.nodeTypes.PixelTexture3D.superClass.call(this, ctx);
        }
    )
);

/* ### TextureCoordinate3D ### */
x3dom.registerNodeType(
    "TextureCoordinate3D",
    "Texturing3D",
    defineClass(x3dom.nodeTypes.X3DTextureCoordinateNode,
        function (ctx) {
            x3dom.nodeTypes.TextureCoordinate3D.superClass.call(this, ctx);

            this.addField_MFVec3f(ctx, 'point', []);
        }
    )
);

/* ### TextureTransform3D ### */
x3dom.registerNodeType(
    "TextureTransform3D",
    "Texturing3D",
    defineClass(x3dom.nodeTypes.X3DTextureTransformNode,
        function (ctx) {
            x3dom.nodeTypes.TextureTransform3D.superClass.call(this, ctx);

            this.addField_SFVec3f(ctx, 'center', 0, 0, 0);
            this.addField_SFRotation(ctx, 'rotation', 0, 0, 1, 0);
            this.addField_SFVec3f(ctx, 'scale', 1, 1, 1);
            this.addField_SFVec3f(ctx, 'translation', 0, 0, 0);
            this.addField_SFRotation(ctx, 'scaleOrientation', 0, 0, 1, 0);
        }
    )
);

/* ### TextureTransformMatrix3D ### */
x3dom.registerNodeType(
    "TextureTransformMatrix3D",
    "Texturing3D",
    defineClass(x3dom.nodeTypes.X3DTextureTransformNode,
        function (ctx) {
            x3dom.nodeTypes.TextureTransformMatrix3D.superClass.call(this, ctx);

            this.addField_SFMatrix4f(ctx, 'matrix', 1, 0, 0, 0,
                                                    0, 1, 0, 0,
                                                    0, 0, 1, 0,
                                                    0, 0, 0, 1);
        }
    )
);

/* ### ImageTextureAtlas ### */
x3dom.registerNodeType(
    "ImageTextureAtlas",
    "Texturing",
    defineClass(x3dom.nodeTypes.Texture,
        function (ctx) {
            x3dom.nodeTypes.ImageTextureAtlas.superClass.call(this, ctx);

            this.addField_SFInt32(ctx, 'numberOfSlices', 0);
            this.addField_SFInt32(ctx, 'slicesOverX', 0);
            this.addField_SFInt32(ctx, 'slicesOverY', 0);
            // Special helper node to represent tiles for volume rendering
        }
    )
);
