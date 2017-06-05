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


/* ### Uniform ### */
x3dom.registerNodeType(
    "Uniform",
    "Shaders",
    defineClass(x3dom.nodeTypes.Field,
        function (ctx) {
            x3dom.nodeTypes.Uniform.superClass.call(this, ctx);
        }
    )
);

/* ### SurfaceShaderTexture ### */
x3dom.registerNodeType(
    "SurfaceShaderTexture",
    "Shaders",
    defineClass(x3dom.nodeTypes.X3DTextureNode,
        function (ctx) {
            x3dom.nodeTypes.SurfaceShaderTexture.superClass.call(this, ctx);

            this.addField_SFInt32(ctx, 'textureCoordinatesId', 0);
            this.addField_SFString(ctx, 'channelMask', "DEFAULT");
            this.addField_SFBool(ctx, 'isSRGB', false);
            this.addField_SFNode('texture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('textureTransform', x3dom.nodeTypes.X3DTextureTransformNode);
        }
    )
);

/* ### X3DShaderNode ### */
x3dom.registerNodeType(
    "X3DShaderNode",
    "Shaders",
    defineClass(x3dom.nodeTypes.X3DAppearanceChildNode,
        function (ctx) {
            x3dom.nodeTypes.X3DShaderNode.superClass.call(this, ctx);

            this.addField_SFString(ctx, 'language', "");
        }
    )
);

/* ### CommonSurfaceShader ### */
x3dom.registerNodeType(
    "CommonSurfaceShader",
    "Shaders",
    defineClass(x3dom.nodeTypes.X3DShaderNode,
        function (ctx) {
            x3dom.nodeTypes.CommonSurfaceShader.superClass.call(this, ctx);

            this.addField_SFInt32(ctx, 'tangentTextureCoordinatesId', -1);
            this.addField_SFInt32(ctx, 'binormalTextureCoordinatesId', -1);
            this.addField_SFVec3f(ctx, 'emissiveFactor', 0, 0, 0);
            this.addField_SFInt32(ctx, 'emissiveTextureId', -1);
            this.addField_SFInt32(ctx, 'emissiveTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'emissiveTextureChannelMask', 'rgb');
            this.addField_SFVec3f(ctx, 'ambientFactor', 0.2, 0.2, 0.2);
            this.addField_SFInt32(ctx, 'ambientTextureId', -1);
            this.addField_SFInt32(ctx, 'ambientTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'ambientTextureChannelMask', 'rgb');
            this.addField_SFVec3f(ctx, 'diffuseFactor', 0.8, 0.8, 0.8);
            this.addField_SFInt32(ctx, 'diffuseTextureId', -1);
            this.addField_SFInt32(ctx, 'diffuseTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'diffuseTextureChannelMask', 'rgb');
            this.addField_SFVec3f(ctx, 'specularFactor', 0, 0, 0);
            this.addField_SFInt32(ctx, 'specularTextureId', -1);
            this.addField_SFInt32(ctx, 'specularTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'specularTextureChannelMask', 'rgb');
            this.addField_SFFloat(ctx, 'shininessFactor', 0.2);
            this.addField_SFInt32(ctx, 'shininessTextureId', -1);
            this.addField_SFInt32(ctx, 'shininessTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'shininessTextureChannelMask', 'a');
            this.addField_SFString(ctx, 'normalFormat', 'UNORM');
            this.addField_SFString(ctx, 'normalSpace', 'TANGENT');
            this.addField_SFInt32(ctx, 'normalTextureId', -1);
            this.addField_SFInt32(ctx, 'normalTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'normalTextureChannelMask', 'rgb');
            this.addField_SFVec3f(ctx, 'reflectionFactor', 0, 0, 0);
            this.addField_SFInt32(ctx, 'reflectionTextureId', -1);
            this.addField_SFInt32(ctx, 'reflectionTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'reflectionTextureChannelMask', 'rgb');
            this.addField_SFVec3f(ctx, 'transmissionFactor', 0, 0, 0);
            this.addField_SFInt32(ctx, 'transmissionTextureId', -1);
            this.addField_SFInt32(ctx, 'transmissionTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'transmissionTextureChannelMask', 'rgb');
            this.addField_SFVec3f(ctx, 'environmentFactor', 1, 1, 1);
            this.addField_SFInt32(ctx, 'environmentTextureId', -1);
            this.addField_SFInt32(ctx, 'environmentTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'environmentTextureChannelMask', 'rgb');
            this.addField_SFFloat(ctx, 'relativeIndexOfRefraction', 1);
            this.addField_SFFloat(ctx, 'fresnelBlend', 0);
            this.addField_SFString(ctx, 'displacementAxis', 'y');
            this.addField_SFFloat(ctx, 'displacementFactor', 255.0);
            this.addField_SFInt32(ctx, 'displacementTextureId', -1);
            this.addField_SFInt32(ctx, 'displacementTextureCoordinatesId', 0);
            this.addField_SFNode('emissiveTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('ambientTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('diffuseTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('specularTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('shininessTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('normalTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('reflectionTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('transmissionTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('environmentTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('displacementTexture', x3dom.nodeTypes.X3DTextureNode);
            this.addField_SFNode('diffuseDisplacementTexture', x3dom.nodeTypes.X3DTextureNode);
            //this.addField_MFBool(ctx, 'textureTransformEnabled', []);     // MFBool NYI
            this.addField_SFVec3f(ctx, 'normalScale', 2, 2, 2);
            this.addField_SFVec3f(ctx, 'normalBias', -1, -1, -1);
            this.addField_SFFloat(ctx, 'alphaFactor', 1);
            this.addField_SFBool(ctx, 'invertAlphaTexture', false);
            this.addField_SFInt32(ctx, 'alphaTextureId', -1);
            this.addField_SFInt32(ctx, 'alphaTextureCoordinatesId', 0);
            this.addField_SFString(ctx, 'alphaTextureChannelMask', 'a');
            this.addField_SFNode('alphaTexture', x3dom.nodeTypes.X3DTextureNode);

            this._dirty = {
                // TODO; cp. Shape, allow for dynamic texture updates in gfx
            };
        },
        {
            getDiffuseMap: function()
            {
                if(this._cf.diffuseTexture.node) {
					this._cf.diffuseTexture.node._cf.texture.node._type = "diffuseMap";
                    return this._cf.diffuseTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },

            getNormalMap: function()
            {
                if(this._cf.normalTexture.node) {
					this._cf.normalTexture.node._cf.texture.node._type = "normalMap";
                    return this._cf.normalTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },

            getAmbientMap: function()
            {
                if(this._cf.ambientTexture.node) {
					this._cf.ambientTexture.node._cf.texture.node._type = "ambientMap";
                    return this._cf.ambientTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },

            getSpecularMap: function()
            {
                if(this._cf.specularTexture.node) {
					this._cf.specularTexture.node._cf.texture.node._type = "specularMap";
                    return this._cf.specularTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },

            getShininessMap: function()
            {
                if(this._cf.shininessTexture.node) {
					this._cf.shininessTexture.node._cf.texture.node._type = "shininessMap";
                    return this._cf.shininessTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },

            getAlphaMap: function()
            {
                if(this._cf.alphaTexture.node) {
					this._cf.alphaTexture.node._cf.texture.node._type = "alphaMap";
                    return this._cf.alphaTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },
            
            getDisplacementMap: function()
            {
                if(this._cf.displacementTexture.node) {
                    this._cf.displacementTexture.node._cf.texture.node._type = "displacementMap";
                    return this._cf.displacementTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },

            getDiffuseDisplacementMap: function()
            {
                if(this._cf.diffuseDisplacementTexture.node) {
                    this._cf.diffuseDisplacementTexture.node._cf.texture.node._type = "diffuseDisplacementMap";
                    return this._cf.diffuseDisplacementTexture.node._cf.texture.node;
                } else {
                    return null;
                }
            },
			
			getTextures: function()
			{
				var textures = [];
				
				var diff = this.getDiffuseMap();
				if(diff) textures.push(diff);
				
				var norm = this.getNormalMap();
				if(norm) textures.push(norm);
				
				var spec = this.getSpecularMap();
				if(spec) textures.push(spec);
        
                var displacement = this.getDisplacementMap();
				if(displacement) textures.push(displacement);

                var diffuseDisplacement = this.getDiffuseDisplacementMap();
                if(diffuseDisplacement) textures.push(diffuseDisplacement);
				
				return textures;
			}
        }
    )
);

/* ### ComposedShader ### */
x3dom.registerNodeType(
    "ComposedShader",
    "Shaders",
    defineClass(x3dom.nodeTypes.X3DShaderNode,
        function (ctx) {
            x3dom.nodeTypes.ComposedShader.superClass.call(this, ctx);

            this.addField_MFNode('fields', x3dom.nodeTypes.Field);
            this.addField_MFNode('parts', x3dom.nodeTypes.ShaderPart);

            // shortcut to shader parts
            this._vertex = null;
            this._fragment = null;

            if (!x3dom.nodeTypes.ComposedShader.ShaderInfoMsgShown) {
                x3dom.debug.logInfo("Current ComposedShader node implementation limitations:\n" +
                    "Vertex attributes (if given in the standard X3D fields 'coord', 'color', " +
                    "'normal', 'texCoord'), matrices and texture are provided as follows...\n" +
                    "(see also <a href='http://x3dom.org/x3dom/doc/help/composedShader.html'>" +
                    "http://x3dom.org/x3dom/doc/help/composedShader.html</a>)\n" +
                    "    attribute vec3 position;\n" +
                    "    attribute vec3 normal;\n" +
                    "    attribute vec2 texcoord;\n" +
                    "    attribute vec3 color;\n" +
                    "    uniform mat4 modelViewProjectionMatrix;\n" +
                    "    uniform mat4 modelViewMatrix;\n" +
					"    uniform mat4 normalMatrix;\n" +
					"    uniform mat4 viewMatrix;\n" +
                    "    uniform sampler2D tex;\n");
                x3dom.nodeTypes.ComposedShader.ShaderInfoMsgShown = true;
            }
        },
        {
            nodeChanged: function()
            {
                var i, n = this._cf.parts.nodes.length;

                for (i=0; i<n; i++)
                {
                    if (this._cf.parts.nodes[i]._vf.type.toLowerCase() == 'vertex') {
                        this._vertex = this._cf.parts.nodes[i];
                    }
                    else if (this._cf.parts.nodes[i]._vf.type.toLowerCase() == 'fragment') {
                        this._fragment = this._cf.parts.nodes[i];
                    }
                }

                var ctx = {};
                n = this._cf.fields.nodes.length;

                for (i=0; i<n; i++)
                {
                    var fieldName = this._cf.fields.nodes[i]._vf.name;
                    ctx.xmlNode = this._cf.fields.nodes[i]._xmlNode;

                    var needNode = false;

                    if (ctx.xmlNode === undefined || ctx.xmlNode === null) {
                        ctx.xmlNode = document.createElement("field");
                        needNode = true;
                    }

                    ctx.xmlNode.setAttribute(fieldName, this._cf.fields.nodes[i]._vf.value);

                    var funcName = "this.addField_" + this._cf.fields.nodes[i]._vf.type + "(ctx, name);";
                    var func = new Function('ctx', 'name', funcName);

                    func.call(this, ctx, fieldName);

                    if (needNode) {
                        ctx.xmlNode = null;    // cleanup
                    }
                }
				
				Array.forEach(this._parentNodes, function (app) {
					Array.forEach(app._parentNodes, function (shape) {
						//shape.setAppDirty();
						if (shape._cleanupGLObjects)
						    shape._cleanupGLObjects();
						shape.setAllDirty();
					});
				});	
            },

            fieldChanged: function(fieldName)
            {
                var i, n = this._cf.fields.nodes.length;

                for (i=0; i<n; i++)
                {
                    var field = this._cf.fields.nodes[i]._vf.name;

                    if (field === fieldName)
                    {
                        var msg = this._cf.fields.nodes[i]._vf.value;

                        try {
                            this._vf[field].setValueByStr(msg);
                        }
                        catch (exc1) {
                            try {
                                switch ((typeof(this._vf[field])).toString()) {
                                    case "number":
                                        this._vf[field] = +msg;
                                        break;
                                    case "boolean":
                                        this._vf[field] = (msg.toLowerCase() === "true");
                                        break;
                                    case "string":
                                        this._vf[field] = msg;
                                        break;
                                }
                            }
                            catch (exc2) {
                                x3dom.debug.logError("setValueByStr() NYI for " + typeof(this._vf[field]));
                            }
                        }

                        break;
                    }
                }
                
                if (field === 'url') 
                {
                    Array.forEach(this._parentNodes, function (app) {
    					Array.forEach(app._parentNodes, function (shape) {
    						shape._dirty.shader = true;
    					});
    				});
                }
            },
			
			parentAdded: function(parent)
			{
				//Array.forEach(this._parentNodes, function (app) {
				//	app.nodeChanged();
				//});
				parent.nodeChanged();
			}
        }
    )
);

x3dom.nodeTypes.ComposedShader.ShaderInfoMsgShown = false;


/* ### ShaderPart ### */
x3dom.registerNodeType(
    "ShaderPart",
    "Shaders",
    defineClass(x3dom.nodeTypes.X3DNode,
        function (ctx) {
            x3dom.nodeTypes.ShaderPart.superClass.call(this, ctx);

            this.addField_MFString(ctx, 'url', []);
            this.addField_SFString(ctx, 'type', "VERTEX");

            x3dom.debug.assert(this._vf.type.toLowerCase() == 'vertex' ||
                               this._vf.type.toLowerCase() == 'fragment');
        },
        {
			nodeChanged: function()
            {
                var ctx = {};
                ctx.xmlNode = this._xmlNode;

                if (ctx.xmlNode !== undefined && ctx.xmlNode !== null) 
                {
                    var that = this;

                    if (that._vf.url.length && that._vf.url[0].indexOf('\n') == -1)
                    {
                        var xhr = new XMLHttpRequest();
                        xhr.open("GET", encodeURI(that._nameSpace.getURL(that._vf.url[0])), false);
                        xhr.onload = function() {
                            that._vf.url = new x3dom.fields.MFString( [] );
                            that._vf.url.push(xhr.response);
                        };
                        xhr.onerror = function() {
                            x3dom.debug.logError("Could not load file '" + that._vf.url[0] + "'.");
                        };
                        xhr.send(null);
                    }
                    else
                    {
                        if (that._vf.url.length) {
                            that._vf.url = new x3dom.fields.MFString( [] );
                        }
                        try {
                            that._vf.url.push(ctx.xmlNode.childNodes[1].nodeValue);
                            ctx.xmlNode.removeChild(ctx.xmlNode.childNodes[1]);
                        }
                        catch(e) {
                            Array.forEach( ctx.xmlNode.childNodes, function (childDomNode) {
                                if (childDomNode.nodeType === 3) {
                                    that._vf.url.push(childDomNode.nodeValue);
                                }
                                else if (childDomNode.nodeType === 4) {
                                    that._vf.url.push(childDomNode.data);
                                }
                                childDomNode.parentNode.removeChild(childDomNode);
                            } );
                        }
                    }
                }
                // else hope that url field was already set somehow

                Array.forEach(this._parentNodes, function (shader) {
                    shader.nodeChanged();
                });
			},
			
			fieldChanged: function(fieldName)
            {
                if (fieldName === "url") {
                    Array.forEach(this._parentNodes, function (shader) {
    					shader.fieldChanged("url");
    				});
                }
			},
			
			parentAdded: function(parent)
			{
				//Array.forEach(this._parentNodes, function (shader) {
				//	shader.nodeChanged();
				//});
				parent.nodeChanged();
			}
        }
    )
);

/* ### X3DVertexAttributeNode ### */
x3dom.registerNodeType(
    "X3DVertexAttributeNode",
    "Shaders",
    defineClass(x3dom.nodeTypes.X3DGeometricPropertyNode,
        function (ctx) {
            x3dom.nodeTypes.X3DVertexAttributeNode.superClass.call(this, ctx);

            this.addField_SFString(ctx, 'name', "");
        }
    )
);

/* ### FloatVertexAttribute ### */
x3dom.registerNodeType(
    "FloatVertexAttribute",
    "Shaders",
    defineClass(x3dom.nodeTypes.X3DVertexAttributeNode,
        function (ctx) {
            x3dom.nodeTypes.FloatVertexAttribute.superClass.call(this, ctx);

            this.addField_SFInt32(ctx, 'numComponents', 4);
            this.addField_MFFloat(ctx, 'value', []);
        }
    )
);

