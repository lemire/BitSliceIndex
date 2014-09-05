package me.lemire.tfunction.twobytwo;

import me.lemire.tfunction.Intersections;


public class CounterUnions {

	public static int unite2by2withcounter(final int[] set1,
			final int[] counter1, final int length1, final int[] set2,
			final int[] counter2, final int length2, final int[] buffer,
			final int[] bufcounter) {
		int pos = 0;
		int k1 = 0, k2 = 0;
		if (0 == length1) {
			for (int k = 0; k < length1; ++k) {
				buffer[k] = set1[k];
				bufcounter[k] = counter1[k];
			}
			return length1;
		}
		if (0 == length2) {
			for (int k = 0; k < length2; ++k) {
				buffer[k] = set2[k];
				bufcounter[k] = counter2[k];
			}
			return length2;
		}
		while (true) {
			if (set1[k1] < set2[k2]) {
				buffer[pos] = set1[k1];
				bufcounter[pos] = counter1[k1];
				++pos;
				++k1;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						buffer[pos] = set2[k2];
						bufcounter[pos] = counter2[k2];
						++pos;
					}
					break;
				}
			} else if (set1[k1] == set2[k2]) {
				buffer[pos] = set1[k1];
				bufcounter[pos] = counter1[k1] + counter2[k2];
				++pos;
				++k1;
				++k2;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						buffer[pos] = set2[k2];
						bufcounter[pos] = counter2[k2];
						++pos;
					}
					break;
				}
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = counter1[k1];
						++pos;
					}
					break;
				}
			} else {// if (set1[k1]>set2[k2]) {
				buffer[pos] = set2[k2];
				bufcounter[pos] = counter2[k2];
				++pos;
				++k2;
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = counter1[k1];
						++pos;
					}
					break;
				}
			}
		}
		return pos;
	}

	public static int unite2by2withcounter(final int[] set1,
			final int[] counter1, final int length1, final int[] set2,
			 final int length2, final int[] buffer,
			final int[] bufcounter) {
		int pos = 0;
		int k1 = 0, k2 = 0;
		if (0 == length1) {
			for (int k = 0; k < length1; ++k) {
				buffer[k] = set1[k];
				bufcounter[k] = counter1[k];
			}
			return length1;
		}
		if (0 == length2) {
			for (int k = 0; k < length2; ++k) {
				buffer[k] = set2[k];
				bufcounter[k] = 1;
			}
			return length2;
		}
		while (true) {
			if (set1[k1] < set2[k2]) {
				buffer[pos] = set1[k1];
				bufcounter[pos] = counter1[k1];
				++pos;
				++k1;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						buffer[pos] = set2[k2];
						bufcounter[pos] = 1;
						++pos;
					}
					break;
				}
			} else if (set1[k1] == set2[k2]) {
				buffer[pos] = set1[k1];
				bufcounter[pos] = counter1[k1] + 1;
				++pos;
				++k1;
				++k2;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						buffer[pos] = set2[k2];
						bufcounter[pos] = 1;
						++pos;
					}
					break;
				}
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = counter1[k1];
						++pos;
					}
					break;
				}
			} else {// if (set1[k1]>set2[k2]) {
				buffer[pos] = set2[k2];
				bufcounter[pos] = 1;
				++pos;
				++k2;
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = counter1[k1];
						++pos;
					}
					break;
				}
			}
		}
		return pos;
	}

	public static int unite2by2withcounter(final int[] set1,
			 final int length1, final int[] set2,
			 final int length2, final int[] buffer,
			final int[] bufcounter) {
		int pos = 0;
		int k1 = 0, k2 = 0;
		if (0 == length1) {
			for (int k = 0; k < length1; ++k) {
				buffer[k] = set1[k];
				bufcounter[k] = 1;
			}
			return length1;
		}
		if (0 == length2) {
			for (int k = 0; k < length2; ++k) {
				buffer[k] = set2[k];
				bufcounter[k] = 1;
			}
			return length2;
		}
		while (true) {
			if (set1[k1] < set2[k2]) {
				buffer[pos] = set1[k1];
				bufcounter[pos] = 1;
				++pos;
				++k1;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						buffer[pos] = set2[k2];
						bufcounter[pos] = 1;
						++pos;
					}
					break;
				}
			} else if (set1[k1] == set2[k2]) {
				buffer[pos] = set1[k1];
				bufcounter[pos] = 1 + 1;
				++pos;
				++k1;
				++k2;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						buffer[pos] = set2[k2];
						bufcounter[pos] = 1;
						++pos;
					}
					break;
				}
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = 1;
						++pos;
					}
					break;
				}
			} else {// if (set1[k1]>set2[k2]) {
				buffer[pos] = set2[k2];
				bufcounter[pos] = 1;
				++pos;
				++k2;
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = 1;
						++pos;
					}
					break;
				}
			}
		}
		return pos;
	}

        public static int galloping2by2(final int[] smallset, final int[] counter,
                       final int smalllength, final int[] largeset, final int largelength,
                       final int[] buffer, final int[] bufcounter, int threshold) {
                if(threshold <= 1)
                        throw new RuntimeException("Your threshold makes no sense "+threshold);
                if (0 == smalllength)
                       return 0;
               int k1 = 0;
               int k2 = 0;
               int pos = 0;
               mainwhile: while (true) {
                       if (largeset[k1] < smallset[k2]) {
                               k1 = Intersections.advanceUntil(largeset, k1, largelength, smallset[k2]);
                               if (k1 == largelength)
                                       break mainwhile;
                       }
                       if (smallset[k2] < largeset[k1]) {
                               //
                               if(counter[k2]  >= threshold) {
                                       buffer[pos] = smallset[k2];
                                       bufcounter[pos] = counter[k2];
                                       ++pos;                                       
                               }
                               ++k2;
                               if (k2 == smalllength)
                                       break mainwhile;
                       } else {
                               // (set2[k2] == set1[k1]) 
                               //if(counter[k2] +1 < threshold)
                               //        throw new RuntimeException("bug2");
                               
                               buffer[pos] = smallset[k2];
                               bufcounter[pos] = counter[k2] + 1;
                               ++pos;
                               ++k2;
                               if (k2 == smalllength)
                                       break;
                               k1 = Intersections.advanceUntil(largeset, k1, largelength, smallset[k2]);
                               if (k1 == largelength)
                                       break mainwhile;

                       }

               }
               while(k2 < smalllength) {
                       if(counter[k2]>=threshold) {
                               buffer[pos] = smallset[k2];
                               bufcounter[pos] = counter[k2];
                               ++pos;                                       
                       }
                       ++k2;
               }
               return pos;
       }
        public static int smartunite2by2withcounterandthreshold(final int[] set1,
                final int[] counter1, final int length1, final int[] set2,
                 final int length2, final int[] buffer,
                final int[] bufcounter, int threshold) {
                if(length1 * 64 < length2)
                        return galloping2by2( set1,counter1, length1, set2,length2,buffer,bufcounter, threshold);
                return unite2by2withcounterandthreshold(set1,counter1, length1, set2,length2,buffer,bufcounter, threshold);
        }
	public static int unite2by2withcounterandthreshold(final int[] set1,
			final int[] counter1, final int length1, final int[] set2,
			 final int length2, final int[] buffer,
			final int[] bufcounter, int threshold) {
			if(threshold <= 1)
				throw new RuntimeException("Your threshold makes no sense "+threshold);
			int pos = 0;
			int k1 = 0, k2 = 0;
			if (0 == length1) {
				for (int k = 0; k < length1; ++k) {
					if(counter1[k]>=threshold) {
						buffer[pos] = set1[k];
						bufcounter[pos] = counter1[k];
						++pos;
					}
				}
				return pos;
			}
			if (0 == length2) {
				return pos;
			}
			while (true) {
				if (set1[k1] < set2[k2]) {
					if(counter1[k1]>=threshold) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = counter1[k1];
						++pos;
					}
					++k1;
					if (k1 >= length1) {
						break;
					}
				} else if (set1[k1] == set2[k2]) {
					if(counter1[k1] + 1>=threshold) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = counter1[k1] + 1;
						++pos;
					}
					++k1;
					++k2;
					if (k1 >= length1) {
						break;
					}
					if (k2 >= length2) {
						for (; k1 < length1; ++k1) {
							if(counter1[k1]>=threshold) {
								buffer[pos] = set1[k1];
								bufcounter[pos] = counter1[k1];
								++pos;
							}
						}
						break;
					}
				} else {// if (set1[k1]>set2[k2]) {
					++k2;
					if (k2 >= length2) {
						for (; k1 < length1; ++k1) {
							if(counter1[k1]>=threshold) {
								buffer[pos] = set1[k1];
								bufcounter[pos] = counter1[k1];
								++pos;
							}
						}
						break;
					}
				}
			}
			return pos;
		}
	
	public static int unite2by2withcounterandthreshold(final int[] set1,
			final int length1, final int[] set2,
			 final int length2, final int[] buffer,
			final int[] bufcounter, int threshold) {
			if(threshold <= 1)
				throw new RuntimeException("Your threshold makes no sense "+threshold);
			int pos = 0;
			int k1 = 0, k2 = 0;
			if (0 == length1 || threshold>2 || 0 == length2) {
				return pos;
			}
			while (true) {
				if (set1[k1] < set2[k2]) {
					++k1;
					if (k1 >= length1) {
						break;
					}
				} else if (set1[k1] == set2[k2]) {
					//if(2>=threshold) {
						buffer[pos] = set1[k1];
						bufcounter[pos] = 2;
						++pos;
					//}
					++k1;
					++k2;
					if (k1 >= length1) {
						break;
					}
					if (k2 >= length2) {
						//for (; k1 < length1; ++k1) {
						//}
						break;
					}
				} else {// if (set1[k1]>set2[k2]) {
					++k2;
					if (k2 >= length2) {
						break;
					}
				}
			}
			return pos;
		}
		
	public static int unite2by2withcounterandthreshold(final int[] set1,
			final int[] counter1, final int length1, final int[] set2,
			final int[] counter2, final int length2, final int[] buffer,
			final int[] bufcounter, int threshold) {
		if(threshold <= 1)
			throw new RuntimeException("Your threshold makes no sense "+threshold);
		int pos = 0;
		int k1 = 0, k2 = 0;
		if (0 == length1) {
			for (int k = 0; k < length1; ++k) {
				if(counter1[k]>=threshold) {
					buffer[pos] = set1[k];
					bufcounter[pos] = counter1[k];
					++pos;
				}
			}
			return pos;
		}
		if (0 == length2) {
			for (int k = 0; k < length2; ++k) {
				if(counter2[k]>=threshold) {
					buffer[pos] = set2[k];
					bufcounter[pos] = counter2[k];
					++pos;
				}
			}
			return pos;
		}
		while (true) {
			if (set1[k1] < set2[k2]) {
				if(counter1[k1]>=threshold) {
					buffer[pos] = set1[k1];
					bufcounter[pos] = counter1[k1];
					++pos;
				}
				++k1;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						if(counter2[k2]>=threshold) {
									buffer[pos] = set2[k2];
									bufcounter[pos] = counter2[k2];
									++pos;
						}
					}
					break;
				}
			} else if (set1[k1] == set2[k2]) {
				if(counter1[k1] + counter2[k2]>=threshold) {
					buffer[pos] = set1[k1];
					bufcounter[pos] = counter1[k1] + counter2[k2];
					++pos;
				}
				++k1;
				++k2;
				if (k1 >= length1) {
					for (; k2 < length2; ++k2) {
						if(counter2[k2]>=threshold) {
							buffer[pos] = set2[k2];
							bufcounter[pos] = counter2[k2];
							++pos;
						}
					}
					break;
				}
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						if(counter1[k1]>=threshold) {
							buffer[pos] = set1[k1];
							bufcounter[pos] = counter1[k1];
							++pos;
						}
					}
					break;
				}
			} else {// if (set1[k1]>set2[k2]) {
				if(counter2[k2]>=threshold) {
					buffer[pos] = set2[k2];
					bufcounter[pos] = counter2[k2];
					++pos;
				}
				++k2;
				if (k2 >= length2) {
					for (; k1 < length1; ++k1) {
						if(counter1[k1]>=threshold) {
							buffer[pos] = set1[k1];
							bufcounter[pos] = counter1[k1];
							++pos;
						}
					}
					break;
				}
			}
		}
		return pos;
	}

}
