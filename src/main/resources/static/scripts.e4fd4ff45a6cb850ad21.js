import{Header,TAGS}from"./Header.js";import{History}from"./History.js";export class Pgn{constructor(r="",e={}){const t="]"===r.trim().substr(-1)?r.length:r.lastIndexOf("]\n\n")+1,s=r.substr(0,t),i=r.substr(t),h=!!e.sloppy;this.header=new Header(s),this.history=new History(i,"1"===this.header.tags[TAGS.SetUp]&&this.header.tags[TAGS.FEN]?this.header.tags[TAGS.FEN]:void 0,h)}render(){let r="";return r+=this.header.render(),r+="\n",r+=this.history.render(),r}}