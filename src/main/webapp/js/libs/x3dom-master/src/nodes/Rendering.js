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


/* ### X3DGeometryNode ### */
x3dom.registerNodeType(
    "X3DGeometryNode",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DNode,
        function (ctx) {
            x3dom.nodeTypes.X3DGeometryNode.superClass.call(this, ctx);

            this.addField_SFBool(ctx, 'solid', true);
            this.addField_SFBool(ctx, 'ccw', true);
            // Most geo primitives use geo cache and others might later on,
            // but one should be able to disable cache per geometry node.
            this.addField_SFBool(ctx, 'useGeoCache', true);

            // mesh object also holds volume (_vol)
            this._mesh = new x3dom.Mesh(this);
        },
        {
            getVolume: function() {
                // geometry doesn't hold volume, but mesh does
                return this._mesh.getVolume();
            },

            invalidateVolume: function() {
                this._mesh.invalidate();
            },

            getCenter: function() {
                return this._mesh.getCenter();
            },
            
            getDiameter: function() {
                return this._mesh.getDiameter();
            },

            doIntersect: function(line) {
                return this._mesh.doIntersect(line);
            },

            forceUpdateCoverage: function() {
                return false;
            },

            hasIndexOffset: function() {
                return false;
            },

            getColorTexture: function() {
                return null;
            },

            getColorTextureURL: function() {
                return null;
            },

            parentAdded: function(parent) {
                if (x3dom.isa(parent, x3dom.nodeTypes.X3DShapeNode)) {
                    if (parent._cleanupGLObjects) {
                        parent._cleanupGLObjects(true);
                    }
                    parent.setAllDirty();
                    parent.invalidateVolume();
                }
            }
        }
    )
);

/* ### Mesh ### */
x3dom.registerNodeType(
    "Mesh",         // experimental WebSG geo node
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometryNode,
        function (ctx) {
            x3dom.nodeTypes.Mesh.superClass.call(this, ctx);

            this.addField_SFString(ctx, 'primType', "triangle");
            this.addField_MFInt32(ctx, 'index', []);

            this.addField_MFNode('vertexAttributes', x3dom.nodeTypes.X3DVertexAttributeNode);
        },
        {
            nodeChanged: function()
            {
                var time0 = new Date().getTime();

                var i, n = this._cf.vertexAttributes.nodes.length;

                for (i=0; i<n; i++)
                {
                    var name = this._cf.vertexAttributes.nodes[i]._vf.name;

                    switch (name.toLowerCase())
                    {
                        case "position":
                            this._mesh._positions[0] = this._cf.vertexAttributes.nodes[i]._vf.value.toGL();
                            break;
                        case "normal":
                            this._mesh._normals[0] = this._cf.vertexAttributes.nodes[i]._vf.value.toGL();
                            break;
                        case "texcoord":
                            this._mesh._texCoords[0] = this._cf.vertexAttributes.nodes[i]._vf.value.toGL();
                            break;
                        case "color":
                            this._mesh._colors[0] = this._cf.vertexAttributes.nodes[i]._vf.value.toGL();
                            break;
                        default:
                            this._mesh._dynamicFields[name] = {};
                            this._mesh._dynamicFields[name].numComponents =
                                       this._cf.vertexAttributes.nodes[i]._vf.numComponents;
                            this._mesh._dynamicFields[name].value =
                                       this._cf.vertexAttributes.nodes[i]._vf.value.toGL();
                        break;
                    }
                }

                this._mesh._indices[0] = this._vf.index.toGL();

                this.invalidateVolume();

                this._mesh._numFaces = this._mesh._indices[0].length / 3;
                this._mesh._numCoords = this._mesh._positions[0].length / 3;

                var time1 = new Date().getTime() - time0;
                x3dom.debug.logWarning("Mesh load time: " + time1 + " ms");
            }
        }
    )
);

/* ### PointSet ### */
x3dom.registerNodeType(
    "PointSet",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometryNode,
        function (ctx) {
            x3dom.nodeTypes.PointSet.superClass.call(this, ctx);

            this.addField_SFNode('coord', x3dom.nodeTypes.X3DCoordinateNode);
            this.addField_SFNode('color', x3dom.nodeTypes.X3DColorNode);

            this._mesh._primType = 'POINTS';
        },
        {
            nodeChanged: function()
            {
                var time0 = new Date().getTime();

                var coordNode = this._cf.coord.node;
                x3dom.debug.assert(coordNode);
                var positions = coordNode.getPoints();

                var numColComponents = 3;
                var colorNode = this._cf.color.node;
                var colors = new x3dom.fields.MFColor();
                if (colorNode) {
                    colors = colorNode._vf.color;
                    x3dom.debug.assert(positions.length == colors.length);

                    if (x3dom.isa(colorNode, x3dom.nodeTypes.ColorRGBA)) {
                        numColComponents = 4;
                    }
                }

                this._mesh._numColComponents = numColComponents;
                this._mesh._lit = false;

                this._mesh._indices[0] = [];
                this._mesh._positions[0] = positions.toGL();
                this._mesh._colors[0] = colors.toGL();
                this._mesh._normals[0] = [];
                this._mesh._texCoords[0] = [];

                this.invalidateVolume();
                this._mesh._numCoords = this._mesh._positions[0].length / 3;

                var time1 = new Date().getTime() - time0;
                //x3dom.debug.logInfo("Mesh load time: " + time1 + " ms");
            },

            fieldChanged: function(fieldName)
            {
                var pnts = null;
                
                if (fieldName == "coord")
                {
                    pnts = this._cf.coord.node.getPoints();
                    
                    this._mesh._positions[0] = pnts.toGL();

                    this.invalidateVolume();

                    Array.forEach(this._parentNodes, function (node) {					
                        node._dirty.positions = true;
                        node.invalidateVolume();
                    });
                }
                else if (fieldName == "color")
                {
                    pnts = this._cf.color.node._vf.color;
                    
                    this._mesh._colors[0] = pnts.toGL();

                    Array.forEach(this._parentNodes, function (node) {
                        node._dirty.colors = true;
                    });
                }
            }
        }
    )
);

/* ### X3DComposedGeometryNode ### */
x3dom.registerNodeType(
    "X3DComposedGeometryNode",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometryNode,
        function (ctx) {
            x3dom.nodeTypes.X3DComposedGeometryNode.superClass.call(this, ctx);

            this.addField_SFBool(ctx, 'colorPerVertex', true);
            this.addField_SFBool(ctx, 'normalPerVertex', true);
            this.addField_SFString(ctx, 'normalUpdateMode', 'fast');  // none; fast; nice

            this.addField_MFNode('attrib', x3dom.nodeTypes.X3DVertexAttributeNode);

            this.addField_SFNode('coord', x3dom.nodeTypes.X3DCoordinateNode);
            this.addField_SFNode('normal', x3dom.nodeTypes.Normal);
            this.addField_SFNode('color', x3dom.nodeTypes.X3DColorNode);
            this.addField_SFNode('texCoord', x3dom.nodeTypes.X3DTextureCoordinateNode);
        },
        {
            handleAttribs: function()
            {
                //var time0 = new Date().getTime();

                // TODO; handle case that more than 2^16-1 attributes are to be referenced
                var i, n = this._cf.attrib.nodes.length;

                for (i=0; i<n; i++)
                {
                    var name = this._cf.attrib.nodes[i]._vf.name;

                    switch (name.toLowerCase())
                    {
                        case "position":
                            this._mesh._positions[0] = this._cf.attrib.nodes[i]._vf.value.toGL();
                            break;
                        case "normal":
                            this._mesh._normals[0] = this._cf.attrib.nodes[i]._vf.value.toGL();
                            break;
                        case "texcoord":
                            this._mesh._texCoords[0] = this._cf.attrib.nodes[i]._vf.value.toGL();
                            break;
                        case "color":
                            this._mesh._colors[0] = this._cf.attrib.nodes[i]._vf.value.toGL();
                            break;
                        default:
                            this._mesh._dynamicFields[name] = {};
                            this._mesh._dynamicFields[name].numComponents =
                                       this._cf.attrib.nodes[i]._vf.numComponents;
                            this._mesh._dynamicFields[name].value =
                                       this._cf.attrib.nodes[i]._vf.value.toGL();
                        break;
                    }
                }

                //var time1 = new Date().getTime() - time0;
                //x3dom.debug.logInfo("Mesh load time: " + time1 + " ms");
            }
        }
    )
);

/* ### LineSet ### */
x3dom.registerNodeType(
    "LineSet",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometryNode,
        function (ctx) {
            x3dom.nodeTypes.LineSet.superClass.call(this, ctx);

            this.addField_MFInt32(ctx, 'vertexCount', []);

            this.addField_MFNode('attrib', x3dom.nodeTypes.X3DVertexAttributeNode);
            this.addField_SFNode('coord', x3dom.nodeTypes.X3DCoordinateNode);
            this.addField_SFNode('color', x3dom.nodeTypes.X3DColorNode);

            this._mesh._primType = "LINES";
            x3dom.Utils.needLineWidth = true;
        },
        {
            nodeChanged: function() {
                var coordNode = this._cf.coord.node;
                x3dom.debug.assert(coordNode);
                var positions = coordNode.getPoints();

                this._mesh._positions[0] = positions.toGL();

                var colorNode = this._cf.color.node;
                if (colorNode) {
                    var colors = colorNode._vf.color;

                    this._mesh._colors[0] = colors.toGL();

                    var numColComponents = 3;
                    if (x3dom.isa(colorNode, x3dom.nodeTypes.ColorRGBA)) {
                        numColComponents = 4;
                    }
                }

                var cnt = 0;
                this._mesh._indices[0] = [];

                for (var i=0, n=this._vf.vertexCount.length; i<n; i++) {
                    var vc = this._vf.vertexCount[i];
                    if (vc < 2) {
                        x3dom.debug.logError("LineSet.vertexCount must not be smaller than 2!");
                        break;
                    }
                    for (var j=vc-2; j>=0; j--) {
                        this._mesh._indices[0].push(cnt++, cnt);
                        if (j == 0) cnt++;
                    }
                }
            },

            fieldChanged: function(fieldName) {
                if (fieldName == "coord") {
                    var pnts = this._cf.coord.node.getPoints();
                    this._mesh._positions[0] = pnts.toGL();

                    this.invalidateVolume();
                    Array.forEach(this._parentNodes, function (node) {
                        node._dirty.positions = true;
                        node.invalidateVolume();
                    });
                }
                else if (fieldName == "color") {
                    var cols = this._cf.color.node._vf.color;
                    this._mesh._colors[0] = cols.toGL();

                    Array.forEach(this._parentNodes, function (node) {
                        node._dirty.colors = true;
                    });
                }
            }
        }
    )
);


/* ### IndexedLineSet ### */
x3dom.registerNodeType(
    "IndexedLineSet",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometryNode,
        function (ctx) {
            x3dom.nodeTypes.IndexedLineSet.superClass.call(this, ctx);

            this.addField_SFBool(ctx, 'colorPerVertex', true);  // TODO

            this.addField_MFNode('attrib', x3dom.nodeTypes.X3DVertexAttributeNode);
            this.addField_SFNode('coord', x3dom.nodeTypes.X3DCoordinateNode);
            this.addField_SFNode('color', x3dom.nodeTypes.X3DColorNode);

            this.addField_MFInt32(ctx, 'coordIndex', []);
            this.addField_MFInt32(ctx, 'colorIndex', []);

            this._mesh._primType = 'LINES';
            x3dom.Utils.needLineWidth = true;
        },
        {
            nodeChanged: function()
            {
                var time0 = new Date().getTime();

                // this.handleAttribs();

                var indexes = this._vf.coordIndex;
                var colorInd = this._vf.colorIndex;

                var hasColor = false, hasColorInd = false;

                // TODO; implement colorPerVertex also for single index
                var colPerVert = this._vf.colorPerVertex;

                if (colorInd.length > 0)
                {
                    hasColorInd = true;
                }

                var positions, colors;

                var coordNode = this._cf.coord.node;
                x3dom.debug.assert(coordNode);
                
                positions = coordNode.getPoints();

                var numColComponents = 3;
                var colorNode = this._cf.color.node;
                if (colorNode)
                {
                    hasColor = true;
                    colors = colorNode._vf.color;

                    if (x3dom.isa(colorNode, x3dom.nodeTypes.ColorRGBA)) {
                        numColComponents = 4;
                    }
                }
                else {
                    hasColor = false;
                }

                this._mesh._indices[0] = [];
                this._mesh._positions[0] = [];
                this._mesh._colors[0] = [];

                var i, t, cnt, lineCnt;
                var p0, p1, c0, c1;

                // Found MultiIndex Mesh OR LineSet with too many vertices for 16 bit
                if ( (hasColor && hasColorInd) || positions.length > x3dom.Utils.maxIndexableCoords )
                {
                    t = 0;
                    cnt = 0;
                    lineCnt = 0;

                    for (i=0; i < indexes.length; ++i)
                    {
                        if (indexes[i] === -1) {
                            t = 0;
                            continue;
                        }

                        if (hasColorInd) {
                            x3dom.debug.assert(colorInd[i] != -1);
                        }

                        switch (t)
                        {
                            case 0:
                                p0 = +indexes[i];
                                if (hasColorInd && colPerVert) { c0 = +colorInd[i]; }
                                else { c0 = p0; }
                                t = 1;
                                break;
                            case 1:
                                p1 = +indexes[i];
                                if (hasColorInd && colPerVert) { c1 = +colorInd[i]; }
                                else if (hasColorInd && !colPerVert) { c1 = +colorInd[lineCnt]; }
                                else { c1 = p1; }

                                this._mesh._indices[0].push(cnt++, cnt++);

                                this._mesh._positions[0].push(positions[p0].x);
                                this._mesh._positions[0].push(positions[p0].y);
                                this._mesh._positions[0].push(positions[p0].z);
                                this._mesh._positions[0].push(positions[p1].x);
                                this._mesh._positions[0].push(positions[p1].y);
                                this._mesh._positions[0].push(positions[p1].z);

                                if (hasColor) {
                                    if (!colPerVert) {
                                        c0 = c1;
                                    }
                                    this._mesh._colors[0].push(colors[c0].r);
                                    this._mesh._colors[0].push(colors[c0].g);
                                    this._mesh._colors[0].push(colors[c0].b);
                                    this._mesh._colors[0].push(colors[c1].r);
                                    this._mesh._colors[0].push(colors[c1].g);
                                    this._mesh._colors[0].push(colors[c1].b);
                                }

                                t = 2;
                                lineCnt++;
                                break;
                            case 2:
                                p0 = p1;
                                c0 = c1;
                                p1 = +indexes[i];
                                if (hasColorInd && colPerVert) { c1 = +colorInd[i]; }
                                else if (hasColorInd && !colPerVert) { c1 = +colorInd[lineCnt]; }
                                else { c1 = p1; }

                                this._mesh._indices[0].push(cnt++, cnt++);

                                this._mesh._positions[0].push(positions[p0].x);
                                this._mesh._positions[0].push(positions[p0].y);
                                this._mesh._positions[0].push(positions[p0].z);
                                this._mesh._positions[0].push(positions[p1].x);
                                this._mesh._positions[0].push(positions[p1].y);
                                this._mesh._positions[0].push(positions[p1].z);

                                if (hasColor) {
                                    if (!colPerVert) {
                                        c0 = c1;
                                    }
                                    this._mesh._colors[0].push(colors[c0].r);
                                    this._mesh._colors[0].push(colors[c0].g);
                                    this._mesh._colors[0].push(colors[c0].b);
                                    this._mesh._colors[0].push(colors[c1].r);
                                    this._mesh._colors[0].push(colors[c1].g);
                                    this._mesh._colors[0].push(colors[c1].b);
                                }

                                lineCnt++;
                                break;
                            default:
                        }
                    }

                    //if the LineSet is too large for 16 bit indices, split it!
                    if (positions.length > x3dom.Utils.maxIndexableCoords)
                        this._mesh.splitMesh(2);
                } // if isMulti
                else
                {
                    var n = indexes.length;
                    t = 0;

                    for (i=0; i < n; ++i)
                    {
                        if (indexes[i] == -1) {
                            t = 0;
                            continue;
                        }

                        switch (t) {
                        case 0: p0 = +indexes[i]; t = 1; break;
                        case 1: p1 = +indexes[i]; t = 2; this._mesh._indices[0].push(p0, p1); break;
                        case 2: p0 = p1; p1 = +indexes[i]; this._mesh._indices[0].push(p0, p1); break;
                        }
                    }

                    this._mesh._positions[0] = positions.toGL();

                    if (hasColor) {
                        this._mesh._colors[0] = colors.toGL();
                        this._mesh._numColComponents = numColComponents;
                    }
                }

                this.invalidateVolume();
                this._mesh._numCoords = 0;

                for (i=0; i<this._mesh._indices.length; i++) {
                    this._mesh._numCoords += this._mesh._positions[i].length / 3;
                }

                var time1 = new Date().getTime() - time0;
                //x3dom.debug.logInfo("Mesh load time: " + time1 + " ms");
            },

            fieldChanged: function(fieldName)
            {
                var pnts = null;
                
                if (fieldName == "coord")
                {
                    pnts = this._cf.coord.node._vf.point;
                    
                    this._mesh._positions[0] = pnts.toGL();

                    this.invalidateVolume();

                    Array.forEach(this._parentNodes, function (node) {					
                        node._dirty.positions = true;
                        node.invalidateVolume();
                    });
                }
                else if (fieldName == "color")
                {
                    pnts = this._cf.color.node._vf.color;
                    
                    this._mesh._colors[0] = pnts.toGL();

                    Array.forEach(this._parentNodes, function (node) {
                        node._dirty.colors = true;
                    });
                }
                else if (fieldName == "coordIndex") {
                    this._mesh._indices[0] = [];

                    var indexes = this._vf.coordIndex;
                    var p0, p1, t = 0;

                    for (var i=0, n=indexes.length; i < n; ++i) {
                        if (indexes[i] == -1) {
                            t = 0;
                        }
                        else {
                            switch (t) {
                                case 0: p0 = +indexes[i]; t = 1; break;
                                case 1: p1 = +indexes[i]; t = 2; this._mesh._indices[0].push(p0, p1); break;
                                case 2: p0 = p1; p1 = +indexes[i]; this._mesh._indices[0].push(p0, p1); break;
                            }
                        }
                    }

                    this.invalidateVolume();

                    Array.forEach(this._parentNodes, function (node) {
                        node._dirty.indexes = true;
                        node.invalidateVolume();
                    });
                }
            }
        }
    )
);


/* ### IndexedTriangleSet ### */
x3dom.registerNodeType(
    "IndexedTriangleSet",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DComposedGeometryNode,
        function (ctx) {
            x3dom.nodeTypes.IndexedTriangleSet.superClass.call(this, ctx);

            this.addField_MFInt32(ctx, 'index', []);
        },
        {
            nodeChanged: function()
            {
                var time0 = new Date().getTime();

                this.handleAttribs();

				var colPerVert = this._vf.colorPerVertex;
                var normPerVert = this._vf.normalPerVertex;

                var indexes = this._vf.index;

                var hasNormal = false, hasTexCoord = false, hasColor = false;
                var positions, normals, texCoords, colors;

                var coordNode = this._cf.coord.node;
                x3dom.debug.assert(coordNode);
                positions = coordNode._vf.point;

                var normalNode = this._cf.normal.node;
                if (normalNode) {
                    hasNormal = true;
                    normals = normalNode._vf.vector;
                }
                else {
                    hasNormal = false;
                }

                var texMode = "", numTexComponents = 2;
                var texCoordNode = this._cf.texCoord.node;
                if (x3dom.isa(texCoordNode, x3dom.nodeTypes.MultiTextureCoordinate)) {
                    if (texCoordNode._cf.texCoord.nodes.length)
                        texCoordNode = texCoordNode._cf.texCoord.nodes[0];
                }
                if (texCoordNode) {
                    if (texCoordNode._vf.point) {
                        hasTexCoord = true;
                        texCoords = texCoordNode._vf.point;

                        if (x3dom.isa(texCoordNode, x3dom.nodeTypes.TextureCoordinate3D)) {
                            numTexComponents = 3;
                        }
                    }
                    else if (texCoordNode._vf.mode) {
                        texMode = texCoordNode._vf.mode;
                    }
                }
                else {
                    hasTexCoord = false;
                }

                var numColComponents = 3;
                var colorNode = this._cf.color.node;
                if (colorNode) {
                    hasColor = true;
                    colors = colorNode._vf.color;

                    if (x3dom.isa(colorNode, x3dom.nodeTypes.ColorRGBA)) {
                        numColComponents = 4;
                    }
                }
                else {
                    hasColor = false;
                }

                this._mesh._indices[0] = [];
                this._mesh._positions[0] = [];
                this._mesh._normals[0] = [];
                this._mesh._texCoords[0] = [];
                this._mesh._colors[0] = [];

                var i, t, cnt, faceCnt, posMax;
                var p0, p1, p2, n0, n1, n2, t0, t1, t2, c0, c1, c2;

                // if positions array too short add degenerate triangle
                while (positions.length % 3 > 0) {
                    positions.push(positions.length-1);
                }
                posMax = positions.length;

                if (!normPerVert || posMax > x3dom.Utils.maxIndexableCoords)
                {
                    t = 0;
                    cnt = 0;
                    faceCnt = 0;
                    this._mesh._multiIndIndices = [];
                    this._mesh._posSize = positions.length;

                    for (i=0; i < indexes.length; ++i)
                    {
                        // Convert non-triangular polygons to a triangle fan
                        // (TODO: this assumes polygons are convex)
                        
                        if ((i > 0) && (i % 3 === 0 )) {
                            t = 0; 
							faceCnt++;							
                        }					

                        //TODO: OPTIMIZE but think about cache coherence regarding arrays!!!
                        switch (t)
                        {
                            case 0:
                                p0 = +indexes[i];
								if (normPerVert) { 
									 n0 = p0;
								} else if (!normPerVert) {
									n0 = faceCnt;
								}
                                t0 = p0;
                                if (colPerVert) { 
									 c0 = p0;
								} else if (!colPerVert) {
									c0 = faceCnt;
								}
                                t = 1;
                            break;
                            case 1:
                                p1 = +indexes[i];
								if (normPerVert) { 
									 n1 = p1;
								} else if (!normPerVert) {
									n1 = faceCnt;
								}
                                t1 = p1;
                                if (colPerVert) { 
									 c1 = p1;
								} else if (!colPerVert) {
									c1 = faceCnt;
								}
                                t = 2;
                            break;
                            case 2:
                                p2 = +indexes[i];
                                if (normPerVert) { 
									 n2 = p2;
								} else if (!normPerVert) {
									n2 = faceCnt;
								}
                                t2 = p2;
                                if (colPerVert) { 
									 c2 = p2;
								} else if (!colPerVert) {
									c2 = faceCnt;
								}
                                t = 3;

                                this._mesh._indices[0].push(cnt++, cnt++, cnt++);

                                this._mesh._positions[0].push(positions[p0].x);
                                this._mesh._positions[0].push(positions[p0].y);
                                this._mesh._positions[0].push(positions[p0].z);
                                this._mesh._positions[0].push(positions[p1].x);
                                this._mesh._positions[0].push(positions[p1].y);
                                this._mesh._positions[0].push(positions[p1].z);
                                this._mesh._positions[0].push(positions[p2].x);
                                this._mesh._positions[0].push(positions[p2].y);
                                this._mesh._positions[0].push(positions[p2].z);

                                if (hasNormal) {
                                    this._mesh._normals[0].push(normals[n0].x);
                                    this._mesh._normals[0].push(normals[n0].y);
                                    this._mesh._normals[0].push(normals[n0].z);
                                    this._mesh._normals[0].push(normals[n1].x);
                                    this._mesh._normals[0].push(normals[n1].y);
                                    this._mesh._normals[0].push(normals[n1].z);
                                    this._mesh._normals[0].push(normals[n2].x);
                                    this._mesh._normals[0].push(normals[n2].y);
                                    this._mesh._normals[0].push(normals[n2].z);
                                }
                                else {
                                    this._mesh._multiIndIndices.push(p0, p1, p2);
                                    //this._mesh._multiIndIndices.push(cnt-3, cnt-2, cnt-1);
                                }

                                if (hasColor) {
                                    this._mesh._colors[0].push(colors[c0].r);
                                    this._mesh._colors[0].push(colors[c0].g);
                                    this._mesh._colors[0].push(colors[c0].b);
                                    if (numColComponents === 4) {
                                        this._mesh._colors[0].push(colors[c0].a);
                                    }
                                    this._mesh._colors[0].push(colors[c1].r);
                                    this._mesh._colors[0].push(colors[c1].g);
                                    this._mesh._colors[0].push(colors[c1].b);
                                    if (numColComponents === 4) {
                                        this._mesh._colors[0].push(colors[c1].a);
                                    }
                                    this._mesh._colors[0].push(colors[c2].r);
                                    this._mesh._colors[0].push(colors[c2].g);
                                    this._mesh._colors[0].push(colors[c2].b);
                                    if (numColComponents === 4) {
                                        this._mesh._colors[0].push(colors[c2].a);
                                    }
                                }

                                if (hasTexCoord) {
                                    this._mesh._texCoords[0].push(texCoords[t0].x);
                                    this._mesh._texCoords[0].push(texCoords[t0].y);
                                    if (numTexComponents === 3) {
                                        this._mesh._texCoords[0].push(texCoords[t0].z);
                                    }
                                    this._mesh._texCoords[0].push(texCoords[t1].x);
                                    this._mesh._texCoords[0].push(texCoords[t1].y);
                                    if (numTexComponents === 3) {
                                        this._mesh._texCoords[0].push(texCoords[t1].z);
                                    }
                                    this._mesh._texCoords[0].push(texCoords[t2].x);
                                    this._mesh._texCoords[0].push(texCoords[t2].y);
                                    if (numTexComponents === 3) {
                                        this._mesh._texCoords[0].push(texCoords[t2].z);
                                    }
                                }

                                //faceCnt++;
                            break;
                            default:
                        }
                    }

                    if (!hasNormal) {
                        this._mesh.calcNormals(normPerVert ? Math.PI : 0);
                    }
                    if (!hasTexCoord) {
                        this._mesh.calcTexCoords(texMode);
                    }

                    this._mesh.splitMesh();

                    //x3dom.debug.logInfo(this._mesh._indices.length);
                } // if isMulti
                else
                {
					faceCnt = 0;
					for (i=0; i<indexes.length; i++)
					{
						if ((i > 0) && (i % 3 === 0 )) {                   
							faceCnt++;							
                        }	
						
						this._mesh._indices[0].push(indexes[i]);
							
						if(!normPerVert && hasNormal) {
							this._mesh._normals[0].push(normals[faceCnt].x);
							this._mesh._normals[0].push(normals[faceCnt].y);
							this._mesh._normals[0].push(normals[faceCnt].z);
						}
						if(!colPerVert && hasColor) {								
							this._mesh._colors[0].push(colors[faceCnt].r);
							this._mesh._colors[0].push(colors[faceCnt].g);
							this._mesh._colors[0].push(colors[faceCnt].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(colors[faceCnt].a);
							}   
						}  				
					}
               
                    this._mesh._positions[0] = positions.toGL();
                    
                    if (hasNormal) {
                        this._mesh._normals[0] = normals.toGL();
                    }
                    else {
                        this._mesh.calcNormals(normPerVert ? Math.PI : 0);
                    }
                    
                    if (hasTexCoord) {
                        this._mesh._texCoords[0] = texCoords.toGL();
                        this._mesh._numTexComponents = numTexComponents;
                    }
                    else {
                        this._mesh.calcTexCoords(texMode);
                    }
                    
                    if (hasColor && colPerVert) {
                        this._mesh._colors[0] = colors.toGL();
                        this._mesh._numColComponents = numColComponents;
                    }
                }

                this.invalidateVolume();

                this._mesh._numFaces = 0;
                this._mesh._numCoords = 0;
                for (i=0; i<this._mesh._indices.length; i++) {
                    this._mesh._numFaces += this._mesh._indices[i].length / 3;
                    this._mesh._numCoords += this._mesh._positions[i].length / 3;
                }

                var time1 = new Date().getTime() - time0;
                //x3dom.debug.logInfo("Mesh load time: " + time1 + " ms");
            },

            fieldChanged: function(fieldName)
            {
                var pnts = this._cf.coord.node._vf.point;
                
                if ( pnts.length > x3dom.Utils.maxIndexableCoords )  // are there other problematic cases?
                {
					// TODO; implement
                    x3dom.debug.logWarning("IndexedTriangleSet: fieldChanged with " + 
                                           "too many coordinates not yet implemented!");
                    return;
                }
                
                if (fieldName == "coord")
                {
                    this._mesh._positions[0] = pnts.toGL();
                    
                    // tells the mesh that its bbox requires update
                    this.invalidateVolume();

                    Array.forEach(this._parentNodes, function (node) {					
                        node._dirty.positions = true;
                        node.invalidateVolume();
                    });
                }
                else if (fieldName == "color")
                {                                      
					pnts = this._cf.color.node._vf.color;
						
					if (this._vf.colorPerVertex) { 
				
						this._mesh._colors[0] = pnts.toGL();	
							
					} else if (!this._vf.colorPerVertex) {
						
						var faceCnt = 0;
						var numColComponents = 3;	
                   		if (x3dom.isa(this._cf.color.node, x3dom.nodeTypes.ColorRGBA)) {
							numColComponents = 4;
						}
							
						this._mesh._colors[0] = [];
							
						var indexes = this._vf.index;
						for (var i=0; i < indexes.length; ++i)
						{
							if ((i > 0) && (i % 3 === 0 )) {                   
								faceCnt++;							
							}	
								
							this._mesh._colors[0].push(pnts[faceCnt].r);
							this._mesh._colors[0].push(pnts[faceCnt].g);
							this._mesh._colors[0].push(pnts[faceCnt].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(pnts[faceCnt].a);
							}  
						}
					}
					Array.forEach(this._parentNodes, function (node) {
						node._dirty.colors = true;
					});
                }
				else if (fieldName == "normal")
                {                 
					pnts = this._cf.normal.node._vf.vector;
						
					if (this._vf.normalPerVertex) { 
						
						this._mesh._normals[0] = pnts.toGL();
							
					} else if (!this._vf.normalPerVertex) {
							
						var indexes = this._vf.index;
						this._mesh._normals[0] = [];
						
						var faceCnt = 0;
						for (var i=0; i < indexes.length; ++i)
						{
							if ((i > 0) && (i % 3 === 0 )) {                   
								faceCnt++;							
							}	
							
							this._mesh._normals[0].push(pnts[faceCnt].x);
							this._mesh._normals[0].push(pnts[faceCnt].y);
							this._mesh._normals[0].push(pnts[faceCnt].z);	
						}
					}

					Array.forEach(this._parentNodes, function (node) {
						 node._dirty.normals = true;
					});
                }
				else if (fieldName == "texCoord")
                {
                    var texCoordNode = this._cf.texCoord.node;
                    if (x3dom.isa(texCoordNode, x3dom.nodeTypes.MultiTextureCoordinate)) {
                        if (texCoordNode._cf.texCoord.nodes.length)
                            texCoordNode = texCoordNode._cf.texCoord.nodes[0];
                    }
                    pnts = texCoordNode._vf.point;
                    
                    this._mesh._texCoords[0] = pnts.toGL();
                    
                    Array.forEach(this._parentNodes, function (node) {
                        node._dirty.texcoords = true;
                    });
                }
                // TODO: index
            }
        }
    )
);


/* ### IndexedTriangleStripSet ### */
x3dom.registerNodeType(
    "IndexedTriangleStripSet",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DComposedGeometryNode,
        function (ctx) {
            x3dom.nodeTypes.IndexedTriangleStripSet.superClass.call(this, ctx);
			
            this.addField_MFInt32(ctx, 'index', []);

            this._hasIndexOffset = false;
            this._indexOffset = null;
        },
        {
            hasIndexOffset: function() {
                return this._hasIndexOffset;
            },

            nodeChanged: function() 
            {
                this.handleAttribs();   // check if method is still functional
	
                var hasNormal = false, hasTexCoord = false, hasColor = false;

                var colPerVert = this._vf.colorPerVertex;
                var normPerVert = this._vf.normalPerVertex;

                var indexes = this._vf.index;
				var positions, normals, texCoords, colors;

                var coordNode = this._cf.coord.node;
                x3dom.debug.assert(coordNode);
                positions = coordNode._vf.point;

                var normalNode = this._cf.normal.node;
                if (normalNode) {
                    hasNormal = true;
                    normals = normalNode._vf.vector;
                }
                else {
                    hasNormal = false;
                }

                var texMode = "", numTexComponents = 2;
                var texCoordNode = this._cf.texCoord.node;
                if (x3dom.isa(texCoordNode, x3dom.nodeTypes.MultiTextureCoordinate)) {
                    if (texCoordNode._cf.texCoord.nodes.length)
                        texCoordNode = texCoordNode._cf.texCoord.nodes[0];
                }
                if (texCoordNode) {
                    if (texCoordNode._vf.point) {
                        hasTexCoord = true;
                        texCoords = texCoordNode._vf.point;

                        if (x3dom.isa(texCoordNode, x3dom.nodeTypes.TextureCoordinate3D)) {
                            numTexComponents = 3;
                        }
                    }
                    else if (texCoordNode._vf.mode) {
                        texMode = texCoordNode._vf.mode;
                    }
                }
                else {
                    hasTexCoord = false;
                }
				this._mesh._numTexComponents = numTexComponents;

                var numColComponents = 3;
                var colorNode = this._cf.color.node;
                if (colorNode) {
                    hasColor = true;
                    colors = colorNode._vf.color;

                    if (x3dom.isa(colorNode, x3dom.nodeTypes.ColorRGBA)) {
                        numColComponents = 4;
                    }
                }
                else {
                    hasColor = false;
                }
				this._mesh._numColComponents = numColComponents;
				
                this._mesh._indices[0] = [];
                this._mesh._positions[0] = [];
                this._mesh._normals[0] = [];
                this._mesh._texCoords[0] = [];
                this._mesh._colors[0] = [];

                this.invalidateVolume();
                this._mesh._numFaces = 0;
                this._mesh._numCoords = 0;
				
				var faceCnt = 0, cnt = 0;
				
				if (hasNormal && positions.length <= x3dom.Utils.maxIndexableCoords)
				{
                    this._hasIndexOffset = true;
                    this._indexOffset = [];
					this._mesh._primType = 'TRIANGLESTRIP';

                    var indexOffset = [ 0 ];
					
					for (i=0; i<indexes.length; i++)
					{
						if (indexes[i] == -1) {
							faceCnt++;
							indexOffset.push(this._mesh._indices[0].length);
						}
						else {
						    this._mesh._indices[0].push(+indexes[i]);
							
							if(!normPerVert) {
								this._mesh._normals[0].push(normals[faceCnt].x);
								this._mesh._normals[0].push(normals[faceCnt].y);
								this._mesh._normals[0].push(normals[faceCnt].z);
							}
							if(!colPerVert) {							
								this._mesh._colors[0].push(colors[faceCnt].r);
								this._mesh._colors[0].push(colors[faceCnt].g);
								this._mesh._colors[0].push(colors[faceCnt].b);
								if (numColComponents === 4) {
									this._mesh._colors[0].push(colors[faceCnt].a);
								}   
							}  
						}
					}
					
					this._mesh._positions[0] = positions.toGL();	
                    
					if(normPerVert) {
						this._mesh._normals[0] = normals.toGL();
					}
                    
                    if (hasTexCoord) {
                        this._mesh._texCoords[0] = texCoords.toGL();
                        this._mesh._numTexComponents = numTexComponents;
                    }
                    else {
                        x3dom.debug.logWarning("IndexedTriangleStripSet: no texCoords given and won't calculate!");
                    }
                    
                    if (hasColor) {
						if(colPerVert) {
							this._mesh._colors[0] = colors.toGL();        
						}     
						this._mesh._numColComponents = numColComponents;
                    }
                    
                    for (i=1; i<indexOffset.length; i++) {
                        var triCnt = indexOffset[i] - indexOffset[i-1];
                        this._indexOffset.push( {
                            count: triCnt,
                            offset: 2 * indexOffset[i-1]
                        } );

                        this._mesh._numFaces += (triCnt - 2);
                    }
                    this._mesh._numCoords = this._mesh._positions[0].length / 3;
				} 
				else 
				{
                    this._hasIndexOffset = false;

				    var p1, p2 , p3, n1, n2, n3, t1, t2, t3, c1, c2, c3;
				    
				    var swapOrder = false;
				    
					for (var i=1; i < indexes.length-2; ++i)
					{
						if (indexes[i+1] == -1) {
							i = i+2;
							faceCnt++;
							continue;
						}
						
						// care for counterclockwise point order
						if (swapOrder) {
    						p1 = indexes[i];
    						p2 = indexes[i-1];
    						p3 = indexes[i+1];
						}
						else {
    						p1 = indexes[i-1];
    						p2 = indexes[i];
    						p3 = indexes[i+1];  
						}
						swapOrder = !swapOrder;
						
						if (normPerVert) { 
							n1 = p1;
							n2 = p2;
							n3 = p3;
						} else if (!normPerVert) {
							n1 = n2 = n3 = faceCnt;
						}
						 
						t1 = p1;
						t2 = p2;
						t3 = p3;

						if (colPerVert) {
							c1 = p1;
							c2 = p2;
							c3 = p3;
						} else if (!colPerVert) { 
							c1 = c2 = c3 = faceCnt;
						}
	
						this._mesh._indices[0].push(cnt++, cnt++, cnt++);				
						
						this._mesh._positions[0].push(positions[p1].x);
						this._mesh._positions[0].push(positions[p1].y);
						this._mesh._positions[0].push(positions[p1].z);
						this._mesh._positions[0].push(positions[p2].x);
						this._mesh._positions[0].push(positions[p2].y);
						this._mesh._positions[0].push(positions[p2].z);
						this._mesh._positions[0].push(positions[p3].x);
						this._mesh._positions[0].push(positions[p3].y);
						this._mesh._positions[0].push(positions[p3].z);
					   
						if (hasNormal) {
							this._mesh._normals[0].push(normals[n1].x);
							this._mesh._normals[0].push(normals[n1].y);
							this._mesh._normals[0].push(normals[n1].z);
							this._mesh._normals[0].push(normals[n2].x);
							this._mesh._normals[0].push(normals[n2].y);
							this._mesh._normals[0].push(normals[n2].z);
							this._mesh._normals[0].push(normals[n3].x);
							this._mesh._normals[0].push(normals[n3].y);
							this._mesh._normals[0].push(normals[n3].z);
						}
	
						if (hasColor) {
							this._mesh._colors[0].push(colors[c1].r);
							this._mesh._colors[0].push(colors[c1].g);
							this._mesh._colors[0].push(colors[c1].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(colors[c1].a);
							}    
							this._mesh._colors[0].push(colors[c2].r);
							this._mesh._colors[0].push(colors[c2].g);
							this._mesh._colors[0].push(colors[c2].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(colors[c2].a);
							}    
							this._mesh._colors[0].push(colors[c3].r);
							this._mesh._colors[0].push(colors[c3].g);
							this._mesh._colors[0].push(colors[c3].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(colors[c3].a);
							}    
						}
	
						if (hasTexCoord) {
							this._mesh._texCoords[0].push(texCoords[t1].x);
							this._mesh._texCoords[0].push(texCoords[t1].y);
							if (numTexComponents === 3) {
								this._mesh._texCoords[0].push(texCoords[t1].z);
							}
							this._mesh._texCoords[0].push(texCoords[t2].x);
							this._mesh._texCoords[0].push(texCoords[t2].y);
							if (numTexComponents === 3) {
								this._mesh._texCoords[0].push(texCoords[t2].z);
							}
							this._mesh._texCoords[0].push(texCoords[t3].x);
							this._mesh._texCoords[0].push(texCoords[t3].y);
							if (numTexComponents === 3) {
								this._mesh._texCoords[0].push(texCoords[t3].z);
							}
						}						
					}
					
					if (!hasNormal) {
						this._mesh.calcNormals(Math.PI);
					}
					
					if (!hasTexCoord) {
					  this._mesh.calcTexCoords(texMode);
					}
			
					this._mesh.splitMesh();

                    this.invalidateVolume();

                    for (i=0; i<this._mesh._indices.length; i++) {
                        this._mesh._numFaces += this._mesh._indices[i].length / 3;
                        this._mesh._numCoords += this._mesh._positions[i].length / 3;
                    }
				}
            },
            
            fieldChanged: function(fieldName)
            {
                if (fieldName != "coord" && fieldName != "normal" &&
    				fieldName != "texCoord" && fieldName != "color")
    			{
    			    x3dom.debug.logWarning("IndexedTriangleStripSet: fieldChanged for " +
    			                           fieldName + " not yet implemented!");
    			    return;
    			}
        		
                var pnts = this._cf.coord.node._vf.point;
                
				if ((this._cf.normal.node === null) || (pnts.length > x3dom.Utils.maxIndexableCoords))
                {
					if (fieldName == "coord") {
						this._mesh._positions[0] = [];
						this._mesh._indices[0] =[];
						this._mesh._normals[0] = [];
						this._mesh._texCoords[0] =[];
			
						var hasNormal = false, hasTexCoord = false, hasColor = false;
	
						var colPerVert = this._vf.colorPerVertex;
						var normPerVert = this._vf.normalPerVertex;
		
						var indexes = this._vf.index;
						var positions, normals, texCoords, colors;
		
						var coordNode = this._cf.coord.node;
						x3dom.debug.assert(coordNode);
						positions = coordNode._vf.point;
		
						var normalNode = this._cf.normal.node;
						if (normalNode) {
							hasNormal = true;
							normals = normalNode._vf.vector;
						}
						else {
							hasNormal = false;
						}
		
						var texMode = "", numTexComponents = 2;
                        var texCoordNode = this._cf.texCoord.node;
                        if (x3dom.isa(texCoordNode, x3dom.nodeTypes.MultiTextureCoordinate)) {
                            if (texCoordNode._cf.texCoord.nodes.length)
                                texCoordNode = texCoordNode._cf.texCoord.nodes[0];
                        }
						if (texCoordNode) {
							if (texCoordNode._vf.point) {
								hasTexCoord = true;
								texCoords = texCoordNode._vf.point;
		
								if (x3dom.isa(texCoordNode, x3dom.nodeTypes.TextureCoordinate3D)) {
									numTexComponents = 3;
								}
							}
							else if (texCoordNode._vf.mode) {
								texMode = texCoordNode._vf.mode;
							}
						}
						else {
							hasTexCoord = false;
						}
						this._mesh._numTexComponents = numTexComponents;
		
						var numColComponents = 3;
						var colorNode = this._cf.color.node;
						if (colorNode) {
							hasColor = true;
							colors = colorNode._vf.color;
		
							if (x3dom.isa(colorNode, x3dom.nodeTypes.ColorRGBA)) {
								numColComponents = 4;
							}
						}
						else {
							hasColor = false;
						}
						this._mesh._numColComponents = numColComponents;
						
						this._mesh._indices[0] = [];
						this._mesh._positions[0] = [];
						this._mesh._normals[0] = [];
						this._mesh._texCoords[0] = [];
						this._mesh._colors[0] = [];
						
						var faceCnt = 0, cnt = 0;
						var p1, p2 , p3, n1, n2, n3, t1, t2, t3, c1, c2, c3;
						var swapOrder = false;
						 
						if ( hasNormal  || hasTexCoord || hasColor) {
							
							for (var i=1; i < indexes.length-2; ++i)
							{
								if (indexes[i+1] == -1) {
									i = i+2;
									faceCnt++;
									continue;
								}
								
								if (swapOrder) {
									p1 = indexes[i];
									p2 = indexes[i-1];
									p3 = indexes[i+1];
								}
								else {
									p1 = indexes[i-1];
									p2 = indexes[i];
									p3 = indexes[i+1];  
								}
								swapOrder = !swapOrder;
								
								if (normPerVert) { 
									n1 = p1;
									n2 = p2;
									n3 = p3;
								} else if (!normPerVert) {
									n1 = n2 = n3 = faceCnt;
								}
								 
								t1 = p1;
								t2 = p2;
								t3 = p3;
		
								if (colPerVert) {
									c1 = p1;
									c2 = p2;
									c3 = p3;
								} else if (!colPerVert) { 
									c1 = c2 = c3 = faceCnt;
								}
			
								this._mesh._indices[0].push(cnt++, cnt++, cnt++);				
								
								this._mesh._positions[0].push(positions[p1].x);
								this._mesh._positions[0].push(positions[p1].y);
								this._mesh._positions[0].push(positions[p1].z);
								this._mesh._positions[0].push(positions[p2].x);
								this._mesh._positions[0].push(positions[p2].y);
								this._mesh._positions[0].push(positions[p2].z);
								this._mesh._positions[0].push(positions[p3].x);
								this._mesh._positions[0].push(positions[p3].y);
								this._mesh._positions[0].push(positions[p3].z);
							   
								if (hasNormal) {
									this._mesh._normals[0].push(normals[n1].x);
									this._mesh._normals[0].push(normals[n1].y);
									this._mesh._normals[0].push(normals[n1].z);
									this._mesh._normals[0].push(normals[n2].x);
									this._mesh._normals[0].push(normals[n2].y);
									this._mesh._normals[0].push(normals[n2].z);
									this._mesh._normals[0].push(normals[n3].x);
									this._mesh._normals[0].push(normals[n3].y);
									this._mesh._normals[0].push(normals[n3].z);
								}
			
								if (hasColor) {
									this._mesh._colors[0].push(colors[c1].r);
									this._mesh._colors[0].push(colors[c1].g);
									this._mesh._colors[0].push(colors[c1].b);
									if (numColComponents === 4) {
										this._mesh._colors[0].push(colors[c1].a);
									}    
									this._mesh._colors[0].push(colors[c2].r);
									this._mesh._colors[0].push(colors[c2].g);
									this._mesh._colors[0].push(colors[c2].b);
									if (numColComponents === 4) {
										this._mesh._colors[0].push(colors[c2].a);
									}    
									this._mesh._colors[0].push(colors[c3].r);
									this._mesh._colors[0].push(colors[c3].g);
									this._mesh._colors[0].push(colors[c3].b);
									if (numColComponents === 4) {
										this._mesh._colors[0].push(colors[c3].a);
									}    
								}
			
								if (hasTexCoord) {
									this._mesh._texCoords[0].push(texCoords[t1].x);
									this._mesh._texCoords[0].push(texCoords[t1].y);
									if (numTexComponents === 3) {
										this._mesh._texCoords[0].push(texCoords[t1].z);
									}
									this._mesh._texCoords[0].push(texCoords[t2].x);
									this._mesh._texCoords[0].push(texCoords[t2].y);
									if (numTexComponents === 3) {
										this._mesh._texCoords[0].push(texCoords[t2].z);
									}
									this._mesh._texCoords[0].push(texCoords[t3].x);
									this._mesh._texCoords[0].push(texCoords[t3].y);
									if (numTexComponents === 3) {
										this._mesh._texCoords[0].push(texCoords[t3].z);
									}
								}						
							}
							
							if (!hasNormal) {
								this._mesh.calcNormals(Math.PI);
							}
							
							if (!hasTexCoord) {
							  this._mesh.calcTexCoords(texMode);
							}
					
							this._mesh.splitMesh();
			
						} else {
							var swapOrder = false;
							for (var i = 1; i < indexes.length; ++i)
							{
								if (indexes[i+1] == -1) {
									i = i+2;
									continue;
								}
								
								if (swapOrder) {
									this._mesh._indices[0].push(indexes[i]);
									this._mesh._indices[0].push(indexes[i-1]);
									this._mesh._indices[0].push(indexes[i+1]);
								}
								else {
									this._mesh._indices[0].push(indexes[i-1]);
									this._mesh._indices[0].push(indexes[i]);
									this._mesh._indices[0].push(indexes[i+1]);
								}
								swapOrder = !swapOrder;
							}
							
							this._mesh._positions[0] = positions.toGL();
			
							if (hasNormal) {
								this._mesh._normals[0] = normals.toGL();
							}
							else {
								this._mesh.calcNormals(Math.PI);
							}
							if (hasTexCoord) {
								this._mesh._texCoords[0] = texCoords.toGL();
								this._mesh._numTexComponents = numTexComponents;
							}
							else {
								this._mesh.calcTexCoords(texMode);
							}
							if (hasColor) {
								this._mesh._colors[0] = colors.toGL();
								this._mesh._numColComponents = numColComponents;
							}
							
						}

                        this.invalidateVolume();
						this._mesh._numFaces = 0;
						this._mesh._numCoords = 0;
						
						for (i=0; i<this._mesh._indices.length; i++) {
							this._mesh._numFaces += this._mesh._indices[i].length / 3;
							this._mesh._numCoords += this._mesh._positions[i].length / 3;
						}
		
						Array.forEach(this._parentNodes, function (node) {
							node.setAllDirty();
                            node.invalidateVolume();
						});
					}
					else if (fieldName == "color") {
						var col = this._cf.color.node._vf.color;
						var faceCnt = 0;
						var c1 = c2 = c3 = 0;
						
						var numColComponents = 3;	
					   
						if (x3dom.isa(this._cf.color.node, x3dom.nodeTypes.ColorRGBA)) {
							numColComponents = 4;
						}
						
						this._mesh._colors[0] = [];
						
						var indexes = this._vf.index;
						var swapOrder = false;
						
						for (i=1; i < indexes.length-2; ++i)
						{
							if (indexes[i+1] == -1) {
								i = i+2;
								faceCnt++;
								continue;
							}
								
							if (this._vf.colorPerVertex) { 
								if (swapOrder) {
									c1 = indexes[i];
									c2 = indexes[i-1];
									c3 = indexes[i+1];
								}
								else {
									c1 = indexes[i-1];
									c2 = indexes[i];
									c3 = indexes[i+1];	
								}
								swapOrder = !swapOrder;
							} else if (!this._vf.colorPerVertex) {
								c1 = c2 = c3 = faceCnt;
							}
							this._mesh._colors[0].push(col[c1].r);
							this._mesh._colors[0].push(col[c1].g);
							this._mesh._colors[0].push(col[c1].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(col[c1].a);
							}  
							this._mesh._colors[0].push(col[c2].r);
							this._mesh._colors[0].push(col[c2].g);
							this._mesh._colors[0].push(col[c2].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(col[c2].a);
							}  
							this._mesh._colors[0].push(col[c3].r);
							this._mesh._colors[0].push(col[c3].g);
							this._mesh._colors[0].push(col[c3].b);
							if (numColComponents === 4) {
								this._mesh._colors[0].push(col[c3].a);
							}  
						}
						
						Array.forEach(this._parentNodes, function (node) {
							node._dirty.colors = true;
						}); 
					}  
					else if (fieldName == "normal") {
					    var nor = this._cf.normal.node._vf.vector;
					    var faceCnt = 0;
						var n1 = n2 = n3 = 0;
						
						this._mesh._normals[0] = [];
						
						var indexes = this._vf.index;
						var swapOrder = false;
							
						for (i=1; i < indexes.length-2; ++i)
						{
							if (indexes[i+1] == -1) {
								i = i+2;
								faceCnt++;
								continue;
							}
							
							if (this._vf.normalPerVertex) { 
								if (swapOrder) {
									n1 = indexes[i];
									n2 = indexes[i-1];
									n3 = indexes[i+1];
								}
								else {
									n1 = indexes[i-1];
									n2 = indexes[i];
									n3 = indexes[i+1];	
								}
								swapOrder = !swapOrder;
							} else if (!this._vf.normalPerVertex) {
								n1 = n2 = n3 = faceCnt;
							}
							this._mesh._normals[0].push(nor[n1].x);
							this._mesh._normals[0].push(nor[n1].y);
							this._mesh._normals[0].push(nor[n1].z);
							this._mesh._normals[0].push(nor[n2].x);
							this._mesh._normals[0].push(nor[n2].y);
							this._mesh._normals[0].push(nor[n2].z);
							this._mesh._normals[0].push(nor[n3].x);
							this._mesh._normals[0].push(nor[n3].y);
							this._mesh._normals[0].push(nor[n3].z);
						}
						
						Array.forEach(this._parentNodes, function (node) {
							node._dirty.normals = true;
						}); 
					}
					else if (fieldName == "texCoord") {
                        var texCoordNode = this._cf.texCoord.node;
                        if (x3dom.isa(texCoordNode, x3dom.nodeTypes.MultiTextureCoordinate)) {
                            if (texCoordNode._cf.texCoord.nodes.length)
                                texCoordNode = texCoordNode._cf.texCoord.nodes[0];
                        }
						var tex = texCoordNode._vf.point;
						var t1 = t2 = t3 = 0;
						
						var numTexComponents = 2;	
					   
						if (x3dom.isa(texCoordNode, x3dom.nodeTypes.TextureCoordinate3D)) {
							numTexComponents = 3;
						}
						
						this._mesh._texCoords[0] = [];
						var indexes = this._vf.index;
						var swapOrder = false;
						
						for (i=1; i < indexes.length-2; ++i)
						{
							if (indexes[i+1] == -1) {
								i = i+2;
								continue;
							}
							
							if (swapOrder) {
								t1 = indexes[i];
								t2 = indexes[i-1];
								t3 = indexes[i+1];
							}
							else {
								t1 = indexes[i-1];
								t2 = indexes[i];
								t3 = indexes[i+1];	
							}
							swapOrder = !swapOrder;
							
							this._mesh._texCoords[0].push(tex[t1].x);
							this._mesh._texCoords[0].push(tex[t1].y);
							if (numTexComponents === 3) {
								this._mesh._texCoords[0].push(tex[t1].z);
							}  
							this._mesh._texCoords[0].push(tex[t2].x);
							this._mesh._texCoords[0].push(tex[t2].y);                       
							if (numTexComponents === 3) {
								this._mesh._texCoords[0].tex(col[t2].z);
							}  
							this._mesh._texCoords[0].push(tex[t3].x);
							this._mesh._texCoords[0].push(tex[t3].y);               
							if (numTexComponents === 3) {
								this._mesh._texCoords[0].push(tex[t3].z);
							}  
						}
						
						Array.forEach(this._parentNodes, function (node) {
							node._dirty.texcoords = true;
						}); 
					}
                }
				else
				{
					if (fieldName == "coord")
					{
						this._mesh._positions[0] = pnts.toGL();
						
						// tells the mesh that its bbox requires update
                        this.invalidateVolume();
	
						Array.forEach(this._parentNodes, function (node) {					
							node._dirty.positions = true;
                            node.invalidateVolume();
						});
					}
					else if (fieldName == "color")
					{ 
						pnts = this._cf.color.node._vf.color;
						
						if (this._vf.colorPerVertex) { 
						
							this._mesh._colors[0] = pnts.toGL();	
							
						} else if (!this._vf.colorPerVertex) {
							
							var faceCnt = 0;
							var numColComponents = 3;	
                   
							if (x3dom.isa(this._cf.color.node, x3dom.nodeTypes.ColorRGBA)) {
								numColComponents = 4;
							}
							
							this._mesh._colors[0] = [];
							
							var indexes = this._vf.index;
							for (i=0; i < indexes.length; ++i)
							{
								if (indexes[i] == -1) {	
									faceCnt++;
									continue;
								}
								
								this._mesh._colors[0].push(pnts[faceCnt].r);
								this._mesh._colors[0].push(pnts[faceCnt].g);
								this._mesh._colors[0].push(pnts[faceCnt].b);
								if (numColComponents === 4) {
									this._mesh._colors[0].push(pnts[faceCnt].a);
								}  
							}
						}

						Array.forEach(this._parentNodes, function (node) {
							node._dirty.colors = true;
						});
					}
					else if (fieldName == "normal")
					{
						pnts = this._cf.normal.node._vf.vector;
						
						if (this._vf.normalPerVertex) { 
						
							this._mesh._normals[0] = pnts.toGL();
							
						} else if (!this._vf.normalPerVertex) {
							
							var indexes = this._vf.index;
							this._mesh._normals[0] = [];
							
							var faceCnt = 0;
							for (i=0; i < indexes.length; ++i)
							{
								if (indexes[i] == -1) {					
									faceCnt++;
									continue;
								}
								
								this._mesh._normals[0].push(pnts[faceCnt].x);
								this._mesh._normals[0].push(pnts[faceCnt].y);
								this._mesh._normals[0].push(pnts[faceCnt].z);	
							}
						}

						Array.forEach(this._parentNodes, function (node) {
							 node._dirty.normals = true;
						});
					}
					else if (fieldName == "texCoord")
					{
                        var texCoordNode = this._cf.texCoord.node;
                        if (x3dom.isa(texCoordNode, x3dom.nodeTypes.MultiTextureCoordinate)) {
                            if (texCoordNode._cf.texCoord.nodes.length)
                                texCoordNode = texCoordNode._cf.texCoord.nodes[0];
                        }
						pnts = texCoordNode._vf.point;
						
						this._mesh._texCoords[0] = pnts.toGL();
						
						Array.forEach(this._parentNodes, function (node) {
							node._dirty.texcoords = true;
						});
					}
				}
            }
        }
    )
);


/* ### X3DGeometricPropertyNode ### */
x3dom.registerNodeType(
    "X3DGeometricPropertyNode",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DNode,
        function (ctx) {
            x3dom.nodeTypes.X3DGeometricPropertyNode.superClass.call(this, ctx);
        }
    )
);

/* ### X3DCoordinateNode ### */
x3dom.registerNodeType(
    "X3DCoordinateNode",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometricPropertyNode,
        function (ctx) {
            x3dom.nodeTypes.X3DCoordinateNode.superClass.call(this, ctx);
        },
        {
            fieldChanged: function (fieldName) {
                if (fieldName === "coord" || fieldName === "point") {
                    Array.forEach(this._parentNodes, function (node) {
                        node.fieldChanged("coord");
                    });
                }
            },

            parentAdded: function (parent) {
                if (parent._mesh && parent._cf.coord.node !== this) {
                    parent.fieldChanged("coord");
                }
            }
        }
      )
);


/* ### Coordinate ### */
x3dom.registerNodeType(
    "Coordinate",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DCoordinateNode,
        function (ctx) {
            x3dom.nodeTypes.Coordinate.superClass.call(this, ctx);

            this.addField_MFVec3f(ctx, 'point', []);
        },
        {
            getPoints: function() {
                return this._vf.point;
            }
        }
    )
);


/* ### Normal ### */
x3dom.registerNodeType(
    "Normal",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometricPropertyNode,
        function (ctx) {
            x3dom.nodeTypes.Normal.superClass.call(this, ctx);

            this.addField_MFVec3f(ctx, 'vector', []);
        },
        {
            fieldChanged: function (fieldName) {
                if (fieldName === "normal" || fieldName === "vector") {
                    Array.forEach(this._parentNodes, function (node) {
                        node.fieldChanged("normal");
                    });
                }
            },

            parentAdded: function (parent) {
                if (parent._mesh && //parent._cf.coord.node &&
                    parent._cf.normal.node !== this) {
                    parent.fieldChanged("normal");
                }
            }
        }
    )
);

/* ### X3DColorNode ### */
x3dom.registerNodeType(
    "X3DColorNode",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DGeometricPropertyNode,
        function (ctx) {
            x3dom.nodeTypes.X3DColorNode.superClass.call(this, ctx);
        },
        {
            fieldChanged: function (fieldName) {
                if (fieldName === "color") {
                    Array.forEach(this._parentNodes, function (node) {
                        node.fieldChanged("color");
                    });
                }
            },

            parentAdded: function (parent) {
                if (parent._mesh && //parent._cf.coord.node &&
                    parent._cf.color.node !== this) {
                    parent.fieldChanged("color");
                }
            }
        }
    )
);

/* ### Color ### */
x3dom.registerNodeType(
    "Color",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DColorNode,
        function (ctx) {
            x3dom.nodeTypes.Color.superClass.call(this, ctx);

            this.addField_MFColor(ctx, 'color', []);
        }
    )
);

/* ### ColorRGBA ### */
x3dom.registerNodeType(
    "ColorRGBA",
    "Rendering",
    defineClass(x3dom.nodeTypes.X3DColorNode,
        function (ctx) {
            x3dom.nodeTypes.ColorRGBA.superClass.call(this, ctx);

            this.addField_MFColorRGBA(ctx, 'color', []);
        }
    )
);

